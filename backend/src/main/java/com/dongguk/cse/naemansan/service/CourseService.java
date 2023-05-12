package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.domain.CourseTag;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import com.dongguk.cse.naemansan.dto.CourseDto;
import com.dongguk.cse.naemansan.dto.request.CourseRequestDto;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import com.dongguk.cse.naemansan.repository.CourseRepository;
import com.dongguk.cse.naemansan.repository.CourseTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseTagRepository courseTagRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    // Course Create
    public CourseDto createCourse(Long userId, CourseRequestDto courseRequestDto) {
        Optional<Course> forCheckCourse = courseRepository.findByTitle(courseRequestDto.getTitle());

        if (!forCheckCourse.isEmpty()) {
            log.error("course Name Duplication - user : {}, {}", userId, courseRequestDto);
            return null;
        }

        if (courseRequestDto.getPointDtos().size() == 0) {
            log.error("Not Exist Points - user : {}, {}", userId, courseRequestDto);
            return null;
        }

        // MultiPoint 만드는 과정
        Point points[] = new Point[courseRequestDto.getPointDtos().size()];

        PointDto pointDtoOne = null;
        PointDto pointDtoTwo = null;
        double distance = 0.0;
        for (int i = 0; i < courseRequestDto.getPointDtos().size(); i++) {
            PointDto pointDto = courseRequestDto.getPointDtos().get(i);
            pointDtoOne = pointDtoTwo;
            pointDtoTwo = pointDto;
            distance += getPointDistance(pointDtoOne, pointDtoTwo);
            points[i] = geometryFactory.createPoint(new Coordinate(pointDto.getLongitude(), pointDto.getLatitude()));
        }

        MultiPoint multiPoint = geometryFactory.createMultiPoint(points);

        Course course = courseRepository.save(Course.builder()
                .userId(userId)
                .title(courseRequestDto.getTitle())
                .introduction(courseRequestDto.getIntroduction())
                .startLocationName("임시 시작 위치")
                .startLocation(points[0])
                .locations(multiPoint)
                .distance(distance)
                .status(true).build());

        // CourseTag 등록하는 과정
        List<CourseTagType> courseTagTypes = new ArrayList<>();
        for (CourseTagDto courseTagDto : courseRequestDto.getCourseTags()) {
            courseTagRepository.save(CourseTag.builder()
                    .courseId(course.getId())
                    .courseTagType(courseTagDto.getCourseTagType()).build());
            courseTagTypes.add(courseTagDto.getCourseTagType());
        }

        return CourseDto.UserDataBuilder()
                .course(course)
                .courseTags(courseTagTypes)
                .locations(courseRequestDto.getPointDtos()).build();
    }

    // Course Read
    public CourseDto readCourse(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            log.info("Course ID로 검색한 Course가 존재하지 않습니다. - {}", courseId);
            return null;
        }

        MultiPoint multiPoint = course.get().getLocations();
        List<PointDto> locations = new ArrayList<>();

        for (int i = 0; i < multiPoint.getNumGeometries(); i++) {
            locations.add(new PointDto(multiPoint.getGeometryN(i).getCoordinate().getY(),
                    multiPoint.getGeometryN(i).getCoordinate().getX()));
        }
        return CourseDto.builder()
                .id(course.get().getId())
                .userId(course.get().getUserId())
                .title(course.get().getTitle())
                .createdDateTime(course.get().getCreatedDate())
                .introduction(course.get().getIntroduction())
                .courseTags(null)
                .startLocationName(course.get().getStartLocationName())
                .locations(locations).build();
    }

    public CourseDto updateCourse(Long userId, Long courseId, CourseRequestDto courseRequestDto) {
        log.info("updateCourse - {}", courseRequestDto);
        Optional<Course> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            log.error("Course ID로 검색한 Course가 존재하지 않습니다. - CourseID : {}", courseId);
            return null;
        } else if (course.get().getUserId() != userId) {
            log.error("해당 유저가 만든 산책로가 아닙니다. - UserID : {}", userId);
            return null;
        }
        else if (course.get().getTitle().equals(courseRequestDto.getTitle())) {
            log.error("course Name Duplication - user : {}, {}", userId, courseRequestDto);
            return null;
        }

        course.get().setTitle(courseRequestDto.getTitle());
        course.get().setIntroduction(courseRequestDto.getIntroduction());

        List<CourseTagType> courseTagTypes = new ArrayList<>();
        for (CourseTagDto courseTagDto : courseRequestDto.getCourseTags()) {
            switch (courseTagDto.getStatusType()) {
                case NEW -> {
                    courseTagRepository.save(CourseTag.builder()
                            .courseId(courseId)
                            .courseTagType(courseTagDto.getCourseTagType()).build());
                    courseTagTypes.add(courseTagDto.getCourseTagType());
                }
                case DELETE -> {
                    courseTagRepository.deleteByCourseIdAndCourseTagType(courseId, courseTagDto.getCourseTagType());
                }
                case DEFAULT -> {
                    courseTagTypes.add(courseTagDto.getCourseTagType());
                }
            }
        }

        // Tag 바꾸는거 넣어야 함
        return CourseDto.UserDataBuilder()
                .course(course.get())
                .courseTags(courseTagTypes)
                .locations(courseRequestDto.getPointDtos()).build();
    }

    public Boolean deleteCourse(Long userId, Long courseId) {
        log.info("deleteCourse - UserID : {}, CourseID : {}", userId, courseId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            log.info("Course ID로 검색한 Course가 존재하지 않습니다. - CourseID : {}", courseId);
            return Boolean.FALSE;
        } else if (course.get().getUserId() != userId) {
            log.info("해당 유저가 만든 산책로가 아닙니다. - UserID : {}", userId);
            return Boolean.FALSE;
        }

        courseRepository.deleteById(courseId);
        return Boolean.TRUE;
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

    public List<CourseDto> getCourseListByTag(String tag) {
        CourseTagType courseTagType = CourseTagType.existType(tag);
        if (courseTagType == null) {
            log.error("존재하지 않는 Tag 입니다. - Tag : {}", tag);
            return null;
        }

        List<CourseTag> courseIdList = courseTagRepository.findByCourseTagType(courseTagType);
        List<CourseDto> courseDtos = new ArrayList<>();
        for (CourseTag courseTag : courseIdList) {
            Optional<Course> course = courseRepository.findById(courseTag.getCourseId());
            List<CourseTag> courseTags = courseTagRepository.findByCourseId(courseTag.getCourseId());

            if (course.isEmpty()) {
                log.error("잘못된 CourseId 입니다. - Tag : {}", tag);
                return null;
            }

            MultiPoint multiPoint = course.get().getLocations();
            List<PointDto> locations = new ArrayList<>();

            for (int i = 0; i < multiPoint.getNumGeometries(); i++) {
                locations.add(new PointDto(multiPoint.getGeometryN(i).getCoordinate().getY(),
                        multiPoint.getGeometryN(i).getCoordinate().getX()));
            }

            List<CourseTagType> courseTagTypes = new ArrayList<>();
            for (CourseTag tempCourseTag : courseTags) {
                courseTagTypes.add(tempCourseTag.getCourseTagType());
            }

            courseDtos.add(CourseDto.builder()
                    .id(course.get().getId())
                    .userId(course.get().getUserId())
                    .title(course.get().getTitle())
                    .createdDateTime(course.get().getCreatedDate())
                    .introduction(course.get().getIntroduction())
                    .courseTags(courseTagTypes)
                    .startLocationName(course.get().getStartLocationName())
                    .locations(locations).build());
        }
        return courseDtos;
    }

    public List<CourseDto> getCourseListByLocation(Double latitude, Double longitude) {
        Pageable paging = PageRequest.of(0, 5, Sort.by("distance"));
        Page<Course> pages =  courseRepository.findCourseList(geometryFactory.createPoint(new Coordinate(longitude, latitude)), paging);

        List<Course> courseIds =  pages.getContent();

        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course course : courseIds) {
            MultiPoint multiPoint = course.getLocations();
            List<PointDto> locations = new ArrayList<>();

            for (int i = 0; i < multiPoint.getNumGeometries(); i++) {
                locations.add(new PointDto(multiPoint.getGeometryN(i).getCoordinate().getY(),
                    multiPoint.getGeometryN(i).getCoordinate().getX()));
            }

            List<CourseTag> courseTags = courseTagRepository.findByCourseId(course.getId());
            List<CourseTagType> courseTagTypes = new ArrayList<>();
            for (CourseTag tempCourseTag : courseTags) {
                courseTagTypes.add(tempCourseTag.getCourseTagType());
            }

            courseDtos.add(CourseDto.builder()
                    .id(course.getId())
                    .userId(course.getUserId())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .introduction(course.getIntroduction())
                    .courseTags(courseTagTypes)
                    .startLocationName(course.getStartLocationName())
                    .locations(locations).build());
        }

        return courseDtos;
    }
}