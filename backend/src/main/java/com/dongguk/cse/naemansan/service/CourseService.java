package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.domain.CourseTag;
import com.dongguk.cse.naemansan.domain.Like;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.domain.type.CourseMapping;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
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
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // User 존재유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        // Course Title 중복유무 확인
        courseRepository.findTitle(courseRequestDto.getTitle()).ifPresent(s -> new RestApiException(ErrorCode.DUPLICATION_COURSE_TITLE));

        // Course 등록하는 과정(PointDto2Point 변환)
        Map<String, Object> pointInformation = courseUtil.getPointDto2Point(courseRequestDto.getPointDtos());
        Point point = (Point) pointInformation.get("startLocation");
        MultiPoint multiPoint = (MultiPoint) pointInformation.get("locations");
        double distance = (double) pointInformation.get("distance");

        // Course DB 등록
        Course course = courseRepository.save(Course.builder()
                .courseUser(user)
                .title(courseRequestDto.getTitle())
                .introduction(courseRequestDto.getIntroduction())
                .startLocationName(courseUtil.getLocationName(courseRequestDto.getPointDtos().get(0)))
                .startLocation(point)
                .locations(multiPoint)
                .distance(distance)
                .status(true).build());

        // CourseTag 등록하는 과정(TagDto2Tag and saveAll)
        List<CourseTag> courseTags = courseUtil.getTagDto2Tag(course, courseRequestDto.getCourseTags());
        courseTagRepository.saveAll(courseTags);

        // ResponseDto 를 위한 TagDto 생성
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
        // Course 존재유무 확인
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));

        // Point to PointDto, Tag to TagDto 변환
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
        // User, Course 존재유무, Course Title 중복유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));
        courseRepository.findByIdNotAndTitle(courseId, courseRequestDto.getTitle()).ifPresent(c -> new RestApiException(ErrorCode.DUPLICATION_COURSE_TITLE));

        // Course User 와 Request User 동등유무 확인
        if (course.getCourseUser().getId() != user.getId()) {
            throw new RestApiException(ErrorCode.NOT_EQUAL);
        }

        // Course Data Update
        course.updateCourse(courseRequestDto.getTitle(), courseRequestDto.getIntroduction());

        // Course Tag Data Update, 최적화 필요
        List<CourseTag> courseTagList = new ArrayList<>();
        for (CourseTagDto courseTagDto : courseRequestDto.getCourseTags()) {
            switch (courseTagDto.getTagStatusType()) {
                case NEW -> {
                    courseTagList.add(courseTagRepository.save(CourseTag.builder()
                            .course(course)
                            .courseTagType(courseTagDto.getCourseTagType()).build()));
                }
                case DELETE -> { courseTagRepository.deleteByCourseAndCourseTagType(course, courseTagDto.getCourseTagType()); }
                case DEFAULT -> { courseTagList.add(CourseTag.builder().course(course).courseTagType(courseTagDto.getCourseTagType()).build()); }
            }
        }

        // ResponseDto 를 위한 PointDto 생성
        List<PointDto> locations = courseUtil.getPoint2PointDto(course.getLocations());

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
        // User 존재유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        // Course 존재유무 확인
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));

        if (course.getCourseUser().getId() != user.getId()) {
            throw new RestApiException(ErrorCode.NOT_EQUAL);
        }

        courseRepository.delete(course);

        return Boolean.TRUE;
    }

    public List<CourseListDto> getCourseListByTag(Long userId, String tag) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        // Tag 존재유무 확인
        CourseTagType courseTagType = CourseTagType.existType(tag);
        if (courseTagType == null) {
            throw new RestApiException(ErrorCode.NOT_FOUND_COURSE_TAG);
        }

        List<CourseTag> courseTagList = courseTagRepository.findByCourseTagType(courseTagType);

        List<CourseListDto> courseListDtoList = new ArrayList<>();

        for (CourseTag courseTag : courseTagList) {
            Course course = courseTag.getCourse();

            courseListDtoList.add(CourseListDto.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .courseTags(courseUtil.getTag2TagDto(course.getCourseTags()))
                    .startLocationName(course.getStartLocationName())
                    .distance(course.getDistance())
                    .likeCnt((long) course.getLikes().size())
                    .usingCnt((long) course.getUsingCourses().size())
                    .isLike(courseUtil.existLike(user, course)).build());
        }

        return courseListDtoList;
    }

    public List<CourseListDto> getCourseListByLocation(Long userId, Double latitude, Double longitude) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        List<CourseMapping> courseMappingList =  courseRepository.findCourseList(courseUtil.getLatLng2Point(latitude, longitude), 5L);

        List<CourseListDto> courseListDtoList = new ArrayList<>();
        for (CourseMapping courseMapping : courseMappingList) {
            Course course = courseRepository.findById(courseMapping.getId()).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));

            courseListDtoList.add(CourseListDto.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .courseTags(courseUtil.getTag2TagDto(course.getCourseTags()))
                    .startLocationName(course.getStartLocationName())
                    .distance(course.getDistance())
                    .likeCnt((long) course.getLikes().size())
                    .usingCnt((long) course.getUsingCourses().size())
                    .isLike(courseUtil.existLike(user, course)).build());
        }

        return courseListDtoList;
    }

    public Map<String, Object> likeCourse(Long userId, Long courseId) {
        // User, Course 존재, 이미 Like 했는지 여부 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));
        likeRepository.findByLikeUserAndLikeCourse(user, course).ifPresent(i -> { throw new RestApiException(ErrorCode.EXIST_ENTITY_REQUEST); });

        // Like 저장
        likeRepository.save(Like.builder()
                .likeUser(user)
                .likeCourse(course).build());

        Map<String, Object> map = new HashMap<>();
        map.put("likeCnt", course.getLikes().size());
        map.put("isLike", Boolean.TRUE);

        return map;
    }

    public Map<String, Object> dislikeCourse(Long userId, Long courseId) {
        // User, Course 존재, Like 하지 않았는지 여부 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));
        Like like = likeRepository.findByLikeUserAndLikeCourse(user, course).orElseThrow(() -> new RestApiException(ErrorCode.NOT_EXIST_ENTITY_REQUEST));

        // Like 삭제
        likeRepository.delete(like);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCnt", course.getLikes().size());
        map.put("isLike", Boolean.FALSE);

        return map;
    }
}