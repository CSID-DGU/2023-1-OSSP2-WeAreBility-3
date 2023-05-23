package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.domain.type.CourseMapping;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import com.dongguk.cse.naemansan.dto.response.CourseDetailDto;
import com.dongguk.cse.naemansan.dto.request.CourseRequestDto;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import com.dongguk.cse.naemansan.dto.response.CourseListDto;
import com.dongguk.cse.naemansan.repository.EnrollmentCourseRepository;
import com.dongguk.cse.naemansan.repository.CourseTagRepository;
import com.dongguk.cse.naemansan.repository.LikeRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import com.dongguk.cse.naemansan.util.CourseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    private final UserRepository userRepository;
    private final EnrollmentCourseRepository enrollmentCourseRepository;
    private final CourseTagRepository courseTagRepository;
    private final LikeRepository likeRepository;
    private final CourseUtil courseUtil;


    // Course Create
    public CourseDetailDto createCourse(Long userId, CourseRequestDto courseRequestDto) {
        // User 존재, Course Title 중복유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        enrollmentCourseRepository.findByTitleAndStatus(courseRequestDto.getTitle(), true).ifPresent(s -> new RestApiException(ErrorCode.DUPLICATION_COURSE_TITLE));

        // Course 등록하는 과정(PointDto2Point 변환)
        Map<String, Object> pointInformation = courseUtil.getPointDto2Point(courseRequestDto.getLocations());
        Point point = (Point) pointInformation.get("startLocation");
        MultiPoint multiPoint = (MultiPoint) pointInformation.get("locations");
        double distance = (double) pointInformation.get("distance");

        // Course DB 등록
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.save(EnrollmentCourse.builder()
                .user(user)
                .title(courseRequestDto.getTitle())
                .introduction(courseRequestDto.getIntroduction())
                .startLocationName(courseUtil.getLocationName(courseRequestDto.getLocations().get(0)))
                .startLocation(point)
                .locations(multiPoint)
                .distance(distance).build());

        // CourseTag 등록하는 과정(TagDto2Tag and saveAll)
        List<CourseTag> courseTags = courseUtil.getTagDto2Tag(enrollmentCourse, courseRequestDto.getTags());
        courseTagRepository.saveAll(courseTags);

        // ResponseDto 를 위한 TagDto 생성
        List<CourseTagDto> courseTagDtoList = courseUtil.getTag2TagDto(courseTags);

        return CourseDetailDto.builder()
                .id(enrollmentCourse.getId())
                .userId(enrollmentCourse.getUser().getId())
                .userName(enrollmentCourse.getUser().getName())
                .title(enrollmentCourse.getTitle())
                .createdDateTime(enrollmentCourse.getCreatedDate())
                .introduction(enrollmentCourse.getIntroduction())
                .tags(courseTagDtoList)
                .startLocationName(enrollmentCourse.getStartLocationName())
                .locations(courseRequestDto.getLocations()).build();
    }

    // Course Read
    public CourseDetailDto readCourse(Long courseId) {
        // Course 존재유무 확인
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findByIdAndStatus(courseId, true).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));

        // Point to PointDto, Tag to TagDto 변환
        List<PointDto> locations = courseUtil.getPoint2PointDto(enrollmentCourse.getLocations());
        List<CourseTagDto> courseTagDtoList = courseUtil.getTag2TagDto(enrollmentCourse.getCourseTags());

        return CourseDetailDto.builder()
                .id(enrollmentCourse.getId())
                .userId(enrollmentCourse.getUser().getId())
                .userName(enrollmentCourse.getUser().getName())
                .title(enrollmentCourse.getTitle())
                .createdDateTime(enrollmentCourse.getCreatedDate())
                .introduction(enrollmentCourse.getIntroduction())
                .tags(courseTagDtoList)
                .startLocationName(enrollmentCourse.getStartLocationName())
                .locations(locations).build();
    }

    public CourseDetailDto updateCourse(Long userId, Long courseId, CourseRequestDto courseRequestDto) {
        // User, Course 존재유무, Course Title 중복유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findByIdAndStatus(courseId, true).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));
        enrollmentCourseRepository.findByIdNotAndTitleAndStatus(courseId, courseRequestDto.getTitle(), true).ifPresent(c -> { throw new RestApiException(ErrorCode.DUPLICATION_COURSE_TITLE);});

        // Course User 와 Request User 동등유무 확인
        if (enrollmentCourse.getUser().getId() != user.getId()) {
            throw new RestApiException(ErrorCode.NOT_EQUAL);
        }

        // Course Data Update
        enrollmentCourse.updateCourse(courseRequestDto.getTitle(), courseRequestDto.getIntroduction());

        // Course Tag Data Update, 최적화 필요
        List<CourseTag> courseTagList = new ArrayList<>();
        for (CourseTagDto courseTagDto : courseRequestDto.getTags()) {
            switch (courseTagDto.getTagStatusType()) {
                case NEW -> {
                    courseTagList.add(courseTagRepository.save(CourseTag.builder()
                            .enrollmentCourse(enrollmentCourse)
                            .courseTagType(courseTagDto.getCourseTagType()).build()));
                }
                case DELETE -> { courseTagRepository.deleteByEnrollmentCourseAndCourseTagType(enrollmentCourse, courseTagDto.getCourseTagType()); }
                case DEFAULT -> { courseTagList.add(CourseTag.builder()
                        .enrollmentCourse(enrollmentCourse)
                        .courseTagType(courseTagDto.getCourseTagType()).build()); }
            }
        }

        // ResponseDto 를 위한 PointDto 생성
        List<PointDto> locations = courseUtil.getPoint2PointDto(enrollmentCourse.getLocations());

        return CourseDetailDto.builder()
                .id(enrollmentCourse.getId())
                .userId(enrollmentCourse.getUser().getId())
                .userName(enrollmentCourse.getUser().getName())
                .title(enrollmentCourse.getTitle())
                .createdDateTime(enrollmentCourse.getCreatedDate())
                .introduction(enrollmentCourse.getIntroduction())
                .tags(courseUtil.getTag2TagDto(courseTagList))
                .startLocationName(enrollmentCourse.getStartLocationName())
                .locations(locations).build();
    }

    public Boolean deleteCourse(Long userId, Long courseId) {
        // User 존재, Course 존재 유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findByIdAndStatus(courseId, true).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));

        if (enrollmentCourse.getUser().getId() != user.getId()) {
            throw new RestApiException(ErrorCode.NOT_EQUAL);
        }

        enrollmentCourse.setStatus(false);
        likeRepository.deleteAll(enrollmentCourse.getLikes());

        return Boolean.TRUE;
    }

    public List<CourseListDto> getCourseListByTag(Long userId, String tag) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        // Tag 존재유무 확인
        CourseTagType courseTagType = CourseTagType.existType(tag);
        if (courseTagType == null) {
            throw new RestApiException(ErrorCode.NOT_FOUND_COURSE_TAG);
        }

        Pageable paging = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> pages =  enrollmentCourseRepository.findCourseListByTag(courseTagType, paging);


        List<CourseListDto> courseListDtoList = new ArrayList<>();
        for (EnrollmentCourse enrollmentCourse : pages.getContent()) {
            if (!enrollmentCourse.getStatus()) {
                continue;
            }

            courseListDtoList.add(CourseListDto.builder()
                    .id(enrollmentCourse.getId())
                    .title(enrollmentCourse.getTitle())
                    .createdDateTime(enrollmentCourse.getCreatedDate())
                    .courseTags(courseUtil.getTag2TagDto(enrollmentCourse.getCourseTags()))
                    .startLocationName(enrollmentCourse.getStartLocationName())
                    .distance(enrollmentCourse.getDistance())
                    .likeCnt((long) enrollmentCourse.getLikes().size())
                    .usingCnt((long) enrollmentCourse.getUsingCourses().size())
                    .isLike(courseUtil.existLike(user, enrollmentCourse)).build());
        }

        return courseListDtoList;
    }

    public List<CourseListDto> getCourseListByLocation(Long userId, Double latitude, Double longitude) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        List<CourseMapping> courseMappingList =  enrollmentCourseRepository.findCourseListByLocation(courseUtil.getLatLng2Point(latitude, longitude), 5L);

        List<CourseListDto> courseListDtoList = new ArrayList<>();
        for (CourseMapping courseMapping : courseMappingList) {
            EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(courseMapping.getId()).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));

            // 삭제된 산책로라면 건너뜀
            if (!enrollmentCourse.getStatus()) {
                continue;
            }

            courseListDtoList.add(CourseListDto.builder()
                    .id(enrollmentCourse.getId())
                    .title(enrollmentCourse.getTitle())
                    .createdDateTime(enrollmentCourse.getCreatedDate())
                    .courseTags(courseUtil.getTag2TagDto(enrollmentCourse.getCourseTags()))
                    .startLocationName(enrollmentCourse.getStartLocationName())
                    .distance(enrollmentCourse.getDistance())
                    .likeCnt((long) enrollmentCourse.getLikes().size())
                    .usingCnt((long) enrollmentCourse.getUsingCourses().size())
                    .isLike(courseUtil.existLike(user, enrollmentCourse)).build());
        }

        return courseListDtoList;
    }

    public Map<String, Object> likeCourse(Long userId, Long courseId) {
        // User, Course 존재, 이미 Like 했는지 여부 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findByIdAndStatus(courseId, true).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));
        likeRepository.findByUserAndEnrollmentCourse(user, enrollmentCourse).ifPresent(i -> { throw new RestApiException(ErrorCode.EXIST_ENTITY_REQUEST); });

        // Like 저장
        likeRepository.save(Like.builder()
                .user(user)
                .enrollmentCourse(enrollmentCourse).build());

        Map<String, Object> map = new HashMap<>();
        map.put("likeCnt", enrollmentCourse.getLikes().size());
        map.put("isLike", Boolean.TRUE);

        return map;
    }

    public Map<String, Object> dislikeCourse(Long userId, Long courseId) {
        // User, Course 존재, Like 하지 않았는지 여부 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findByIdAndStatus(courseId, true).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));
        Like like = likeRepository.findByUserAndEnrollmentCourse(user, enrollmentCourse).orElseThrow(() -> new RestApiException(ErrorCode.NOT_EXIST_ENTITY_REQUEST));

        // Like 삭제
        likeRepository.delete(like);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCnt", enrollmentCourse.getLikes().size());
        map.put("isLike", Boolean.FALSE);

        return map;
    }
}