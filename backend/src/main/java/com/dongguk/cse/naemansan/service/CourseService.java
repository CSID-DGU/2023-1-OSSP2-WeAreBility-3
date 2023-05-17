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
import com.dongguk.cse.naemansan.dto.response.CourseListDto;
import com.dongguk.cse.naemansan.repository.CourseRepository;
import com.dongguk.cse.naemansan.repository.CourseTagRepository;
import com.dongguk.cse.naemansan.repository.LikeRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import com.dongguk.cse.naemansan.util.CourseUtil;
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
    private final CourseUtil courseUtil;


    // Course Create
    public CourseDto createCourse(Long userId, CourseRequestDto courseRequestDto) {
        log.info("Create Course - UserID = {}", userId);
        Optional<User> user = userRepository.findById(userId);

        // Title 중복검사, 좌표는 프론트에서 시간으로 Ckeck
        if (!isExistTitle(courseRequestDto.getTitle())) {
            return null;
        }

        // Course 등록하는 과정
        Map<String, Object> pointInformation = courseUtil.getPointDto2Point(courseRequestDto.getPointDtos());
        Point point = (Point) pointInformation.get("startLocation");
        MultiPoint multiPoint = (MultiPoint) pointInformation.get("locations");
        double distance = (double) pointInformation.get("distance");

        Course course = courseRepository.save(Course.builder()
                .courseUser(user.get())
                .title(courseRequestDto.getTitle())
                .introduction(courseRequestDto.getIntroduction())
                .startLocationName(courseUtil.getLocationName(courseRequestDto.getPointDtos().get(0)))
                .startLocation(point)
                .locations(multiPoint)
                .distance(distance)
                .status(true).build());

        // CourseTag 등록하는 과정
        List<CourseTag> courseTags = courseUtil.getTagDto2Tag(course, courseRequestDto.getCourseTags());
        courseTagRepository.saveAll(courseTags);

        List<CourseTagDto> courseTagDtoList = courseUtil.getTag2TagDto(courseTags);
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

        List<PointDto> locations = courseUtil.getPoint2PointDto(course.getLocations());
        List<CourseTagDto> courseTagDtoList = courseUtil.getTag2TagDto(course.getCourseTags());

        return CourseDto.builder()
                .id(course.getId())
                .userId(course.getCourseUser().getId())
                .userName(course.getCourseUser().getName())
                .title(course.getTitle())
                .createdDateTime(course.getCreatedDate())
                .introduction(course.getIntroduction())
                .courseTags(courseTagDtoList)
                .startLocationName(course.getStartLocationName())
                .locations(locations).build();
    }

    public CourseDto updateCourse(Long userId, Long courseId, CourseRequestDto courseRequestDto) {
        log.info("Update Course - CourseID: {}", courseId);
        // 수정할 Course 탐색
        Optional<Course> findCourse = courseRepository.findById(courseId);
        
        // 수정할 Title 중복검사용 Course 탐색
        Optional<Course> findTitle = courseRepository.findByTitle(courseRequestDto.getTitle());

        if (findCourse.isEmpty()) {
            log.error("Course ID로 검색한 Course가 존재하지 않습니다. - CourseID : {}", courseId);
            return null;
        } else if (findCourse.get().getCourseUser().getId() != userId) {
            log.error("해당 유저가 만든 산책로가 아닙니다. - UserID : {}", userId);
            return null;
        } else if (!findTitle.isEmpty() && !findCourse.get().equals(findTitle.get())) {
            log.error("course Name Duplication - user : {}, {}", userId, courseRequestDto);
            return null;
        }

        Course course = findCourse.get();
        course.updateCourse(courseRequestDto.getTitle(), courseRequestDto.getIntroduction());

        List<PointDto> locations = courseUtil.getPoint2PointDto(course.getLocations());
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

        return CourseDto.builder()
                .id(course.getId())
                .userId(course.getCourseUser().getId())
                .userName(course.getCourseUser().getName())
                .title(course.getTitle())
                .createdDateTime(course.getCreatedDate())
                .introduction(course.getIntroduction())
                .courseTags(courseUtil.getTag2TagDto(courseTagList))
                .startLocationName(course.getStartLocationName())
                .locations(locations).build();
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

    public List<CourseListDto> getCourseListByTag(String tag) {
        CourseTagType courseTagType = CourseTagType.existType(tag);
        if (courseTagType == null) {
            log.error("존재하지 않는 Tag 입니다. - Tag : {}", tag);
            return null;
        }

        List<CourseTag> courseTagList = courseTagRepository.findByCourseTagType(courseTagType);

        List<CourseListDto> courseListDtoList = new ArrayList<>();
        for (CourseTag courseTag : courseTagList) {
            Course course = courseTag.getCourse();
            List<CourseTagDto> courseTags = courseUtil.getTag2TagDto(course.getCourseTags());
            courseListDtoList.add(CourseListDto.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .courseTags(courseTags)
                    .startLocationName(course.getStartLocationName())
                    .distance(course.getDistance()).build());
        }

        return courseListDtoList;
    }

    public List<CourseListDto> getCourseListByLocation(Double latitude, Double longitude) {
        Pageable paging = PageRequest.of(0, 5, Sort.by("distance"));
        Page<Course> pages =  courseRepository.findCourseList(courseUtil.getLatLng2Point(latitude, longitude), paging);

        List<Course> courseIds =  pages.getContent();

        List<CourseListDto> courseListDtoList = new ArrayList<>();
        for (Course course : courseIds) {
            List<CourseTagDto> courseTags = courseUtil.getTag2TagDto(course.getCourseTags());
            courseListDtoList.add(CourseListDto.builder()
                            .id(course.getId())
                            .title(course.getTitle())
                            .createdDateTime(course.getCreatedDate())
                            .courseTags(courseTags)
                            .startLocationName(course.getStartLocationName())
                            .distance(course.getDistance()).build());
        }

        return courseListDtoList;
    }

    public Map<String, Object> likeCourse(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (user.isEmpty()) {
            log.error("잘못된 UserID 입니다. - CourseId {}", userId);
            return null;
        }

        if (course.isEmpty()) {
            log.error("잘못된 CourseId 입니다. - CourseId {}", courseId);
            return null;
        }

        Optional<Like> like = likeRepository.findByLikeUserAndLikeCourse(user.get(),course.get());

        if (!like.isEmpty()) {
            log.error("해당 유저는 좋아요 중입니다. - UserID: {}, CourseID: {}", userId, courseId);
            return null;
        }

        likeRepository.save(Like.builder()
                .likeUser(user.get())
                .likeCourse(course.get()).build());

        Map<String, Object> map = new HashMap<>();
        map.put("likeCnt", course.get().getLikes().size());
        map.put("isLike", Boolean.TRUE);

        return map;
    }

    public Map<String, Object> dislikeCourse(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (user.isEmpty()) {
            log.error("잘못된 UserID 입니다. - CourseId {}", userId);
            return null;
        }

        if (course.isEmpty()) {
            log.error("잘못된 CourseId 입니다. - CourseId {}", courseId);
            return null;
        }

        Optional<Like> like = likeRepository.findByLikeUserAndLikeCourse(user.get(),course.get());

        if (like.isEmpty()) {
            log.error("해당 유저는 좋아요하지 않았습니다. - UserID: {}, CourseID: {}", userId, courseId);
            return null;
        }

        likeRepository.deleteByLikeUserAndLikeCourse(user.get(), course.get());

        Map<String, Object> map = new HashMap<>();
        map.put("likeCnt", course.get().getLikes().size());
        map.put("isLike", Boolean.FALSE);

        return map;
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
}