package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.domain.CourseTag;
import com.dongguk.cse.naemansan.domain.Like;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import com.dongguk.cse.naemansan.domain.type.StatusType;
import com.dongguk.cse.naemansan.dto.response.CourseDto;
import com.dongguk.cse.naemansan.dto.request.CourseRequestDto;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import com.dongguk.cse.naemansan.repository.CourseRepository;
import com.dongguk.cse.naemansan.repository.CourseTagRepository;
import com.dongguk.cse.naemansan.repository.LikeRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.BooleanNode;
import org.locationtech.jts.geom.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseTagRepository courseTagRepository;
    private final LikeRepository likeRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    // Course Create
    public CourseDto createCourse(Long userId, CourseRequestDto courseRequestDto) {
        log.info("Create Course - UserID = {}", userId);
        Optional<User> user = userRepository.findById(userId);

        // Title 중복검사, 좌표는 프론트에서 시간으로 Ckeck
        if (!isExistTitle(courseRequestDto.getTitle())) {
            return null;
        }

        // Course 등록하는 과정
        Map<String, Object> pointInformation = getPointDto2Point(courseRequestDto.getPointDtos());
        Point point = (Point) pointInformation.get("startLocation");
        MultiPoint multiPoint = (MultiPoint) pointInformation.get("locations");
        double distance = (double) pointInformation.get("distance");

        Course course = courseRepository.save(Course.builder()
                .courseUser(user.get())
                .title(courseRequestDto.getTitle())
                .introduction(courseRequestDto.getIntroduction())
                .startLocationName("임시 시작 위치")
                .startLocation(point)
                .locations(multiPoint)
                .distance(distance)
                .status(true).build());

        // CourseTag 등록하는 과정
        List<CourseTag> courseTags = getTagDto2Tag(course, courseRequestDto.getCourseTags());
        courseTagRepository.saveAll(courseTags);

        List<CourseTagDto> courseTagDtoList = getTag2TagDto(courseTags);
        return CourseDto.builder()
                .id(course.getId())
                .userId(course.getCourseUser().getId())
                .userName(course.getCourseUser().getName())
                .title(course.getTitle())
                .createdDateTime(course.getCreatedDate())
                .introduction(course.getIntroduction())
                .courseTags(courseTagDtoList)
                .startLocationName(course.getStartLocationName())
                .locations(courseRequestDto.getPointDtos()).build();
    }

    // Course Read
    public CourseDto readCourse(Long courseId) {
        log.info("Read Course - CourseID = {}", courseId);
        Course course = isExistCourse(courseId);

        if (course == null) {
            log.info("Course ID로 검색한 Course가 존재하지 않습니다. - {}", courseId);
            return null;
        }

        List<PointDto> locations = getPoint2PointDto(course.getLocations());
        List<CourseTagDto> courseTagDtoList = getTag2TagDto(course.getCourseTags());

        return CourseDto.builder()
                .id(course.getId())
                .userId(course.getCourseUser().getId())
                .title(course.getTitle())
                .createdDateTime(course.getCreatedDate())
                .introduction(course.getIntroduction())
                .courseTags(courseTagDtoList)
                .startLocationName(course.getStartLocationName())
                .locations(locations).build();
    }

    public CourseDto updateCourse(Long userId, Long courseId, CourseRequestDto courseRequestDto) {
        log.info("Update Course - CourseID: {}", courseId);
        Optional<Course> findCourse = courseRepository.findById(courseId);

        if (findCourse.isEmpty()) {
            log.error("Course ID로 검색한 Course가 존재하지 않습니다. - CourseID : {}", courseId);
            return null;
        } else if (findCourse.get().getCourseUser().getId() != userId) {
            log.error("해당 유저가 만든 산책로가 아닙니다. - UserID : {}", userId);
            return null;
        }
        else if (findCourse.get().getTitle().equals(courseRequestDto.getTitle())) {
            log.error("course Name Duplication - user : {}, {}", userId, courseRequestDto);
            return null;
        }

        Course course = findCourse.get();
        course.updateCourse(courseRequestDto.getTitle(), courseRequestDto.getIntroduction());

        List<CourseTag> courseTagList = new ArrayList<>();
        for (CourseTagDto courseTagDto : courseRequestDto.getCourseTags()) {
            switch (courseTagDto.getStatusType()) {
                case NEW -> {
                    courseTagList.add(courseTagRepository.save(CourseTag.builder()
                            .course(course)
                            .courseTagType(courseTagDto.getCourseTagType()).build()));
                }
                case DELETE -> { courseTagRepository.deleteByCourseAndCourseTagType(course, courseTagDto.getCourseTagType()); }
                case DEFAULT -> { courseTagList.add(CourseTag.builder().course(course).courseTagType(courseTagDto.getCourseTagType()).build()); }
                }
            }

        // Tag 바꾸는거 넣어야 함
        return CourseDto.builder()
                .id(course.getId())
                .userId(course.getCourseUser().getId())
                .title(course.getTitle())
                .createdDateTime(course.getCreatedDate())
                .introduction(course.getIntroduction())
                .courseTags(getTag2TagDto(courseTagList))
                .startLocationName(course.getStartLocationName())
                .locations(courseRequestDto.getPointDtos()).build();
    }

    public Boolean deleteCourse(Long userId, Long courseId) {
        log.info("Delete Course - UserID : {}, CourseID : {}", userId, courseId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            log.info("Course ID로 검색한 Course가 존재하지 않습니다. - {}", courseId);
            return null;
        } else if (course.get().getCourseUser().getId() != userId) {
            log.info("해당 유저가 만든 산책로가 아닙니다. - UserID : {}", userId);
            return Boolean.FALSE;
        }

        courseRepository.deleteById(courseId);
        return Boolean.TRUE;
    }

    public List<CourseDto> getCourseListByTag(String tag) {
        CourseTagType courseTagType = CourseTagType.existType(tag);
        if (courseTagType == null) {
            log.error("존재하지 않는 Tag 입니다. - Tag : {}", tag);
            return null;
        }

        List<CourseTag> courseIdList = courseTagRepository.findByCourseTagType(courseTagType);
        List<CourseDto> courseDtoList = new ArrayList<>();
        for (CourseTag courseTag : courseIdList) {
            Course course = courseTag.getCourse();
            List<PointDto> pointDtoList = getPoint2PointDto(course.getLocations());

            List<CourseTagDto> courseTagDtoList = getTag2TagDto(course.getCourseTags());

            courseDtoList.add(CourseDto.builder()
                    .id(course.getId())
                    .userId(course.getCourseUser().getId())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .introduction(course.getIntroduction())
                    .courseTags(courseTagDtoList)
                    .startLocationName(course.getStartLocationName())
                    .locations(pointDtoList).build());
        }

        return courseDtoList;
    }

    public List<CourseDto> getCourseListByLocation(Double latitude, Double longitude) {
        Pageable paging = PageRequest.of(0, 5, Sort.by("distance"));
        Page<Course> pages =  courseRepository.findCourseList(geometryFactory.createPoint(new Coordinate(longitude, latitude)), paging);

        List<Course> courseIds =  pages.getContent();

        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course course : courseIds) {
            List<PointDto> pointDtoList = getPoint2PointDto(course.getLocations());
            List<CourseTagDto> courseTags = getTag2TagDto(course.getCourseTags());

            courseDtos.add(CourseDto.builder()
                    .id(course.getId())
                    .userId(course.getCourseUser().getId())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .introduction(course.getIntroduction())
                    .courseTags(courseTags)
                    .startLocationName(course.getStartLocationName())
                    .locations(pointDtoList).build());
        }

        return courseDtos;
    }

    public Boolean likeCourse(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (user.isEmpty()) {
            log.error("잘못된 UserID 입니다. - CourseId {}", userId);
            return Boolean.FALSE;
        }

        if (course.isEmpty()) {
            log.error("잘못된 CourseId 입니다. - CourseId {}", courseId);
            return Boolean.FALSE;
        }

        likeRepository.save(Like.builder()
                .likeUser(user.get())
                .likeCourse(course.get()).build());

        return Boolean.TRUE;
    }

    public Boolean dislikeCourse(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (user.isEmpty()) {
            log.error("잘못된 UserID 입니다. - CourseId {}", userId);
            return Boolean.FALSE;
        }

        if (course.isEmpty()) {
            log.error("잘못된 CourseId 입니다. - CourseId {}", courseId);
            return Boolean.FALSE;
        }

        likeRepository.deleteByLikeUserAndLikeCourse(user.get(), course.get());

        return Boolean.TRUE;
    }


    private Boolean isExistTitle(String title) {
        Optional<String> findTitle = courseRepository.findTitle(title);

        if (!findTitle.isEmpty()) {
            log.error("course Name Duplication - Title : {}", title);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private Course isExistCourse(Long courseId) {
        Optional<Course> findCourse = courseRepository.findById(courseId);

        if (findCourse.isEmpty()) {
            log.error("Not Exist Course - CourseID : {}", courseId);
            return null;
        }

        return findCourse.get();
    }

    private double getPointDistance(PointDto pointDtoOne, PointDto pointDtoTwo) {
        if (pointDtoOne == null || pointDtoTwo == null) {
            return 0.0;
        }

        double theta = pointDtoOne.getLongitude() - pointDtoTwo.getLongitude();
        double distance = Math.sin(deg2rad(pointDtoOne.getLatitude())) * Math.sin(deg2rad(pointDtoTwo.getLatitude()))
                + Math.cos(deg2rad(pointDtoOne.getLatitude())) * Math.cos(deg2rad(pointDtoTwo.getLatitude()))
                * Math.cos(deg2rad(theta));

        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = distance * 60 * 1.1515 * 1609.344;
        return distance;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private Map<String, Object> getPointDto2Point(List<PointDto> inputPoints) {
        Map<String, Object> map = new HashMap<>();

        // MultiPoint 만드는 과정
        Point points[] = new Point[inputPoints.size()];

        PointDto pointDtoOne = null;
        PointDto pointDtoTwo = null;
        double distance = 0.0;
        for (int i = 0; i < inputPoints.size(); i++) {
            PointDto pointDto = inputPoints.get(i);
            pointDtoOne = pointDtoTwo;
            pointDtoTwo = pointDto;
            distance += getPointDistance(pointDtoOne, pointDtoTwo);
            points[i] = geometryFactory.createPoint(new Coordinate(pointDto.getLongitude(), pointDto.getLatitude()));
        }

        MultiPoint multiPoint = geometryFactory.createMultiPoint(points);

        map.put("startLocation", points[0]);
        map.put("locations", multiPoint);
        map.put("distance", distance);

        return map;
    }

    private List<PointDto> getPoint2PointDto(MultiPoint multiPoint) {
        List<PointDto> locations = new ArrayList<>();

        for (int i = 0; i < multiPoint.getNumGeometries(); i++) {
            locations.add(new PointDto(multiPoint.getGeometryN(i).getCoordinate().getY(),
                    multiPoint.getGeometryN(i).getCoordinate().getX()));
        }

        return locations;
    }

    private List<CourseTag> getTagDto2Tag(Course course, List<CourseTagDto> dtoList) {
        List<CourseTag> tagList = new ArrayList<>();

        for (CourseTagDto courseTagDto : dtoList) {
            tagList.add(CourseTag.builder()
                    .course(course).courseTagType(courseTagDto.getCourseTagType()).build());
        }

        return tagList;
    }

    private List<CourseTagDto> getTag2TagDto(List<CourseTag> tagList) {
        List<CourseTagDto> dtoList = new ArrayList<>();

        for (CourseTag courseTag : tagList) {
            dtoList.add(CourseTagDto.builder()
                    .courseTagType(courseTag.getCourseTagType())
                    .statusType(StatusType.DEFAULT).build());
        }

        return dtoList;
    }
}