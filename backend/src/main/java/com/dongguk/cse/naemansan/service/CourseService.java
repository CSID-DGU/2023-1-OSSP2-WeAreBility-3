package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.domain.type.CourseMapping;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import com.dongguk.cse.naemansan.dto.response.EnrollmentCourseDetailDto;
import com.dongguk.cse.naemansan.dto.request.CourseRequestDto;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import com.dongguk.cse.naemansan.dto.response.EnrollmentCourseListDto;
import com.dongguk.cse.naemansan.dto.response.IndividualCourseListDto;
import com.dongguk.cse.naemansan.repository.*;
import com.dongguk.cse.naemansan.util.CourseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    private final IndividualCourseRepository individualCourseRepository;
    private final CourseTagRepository courseTagRepository;
    private final LikeRepository likeRepository;
    private final CourseUtil courseUtil;


    // Course Create
    public EnrollmentCourseDetailDto createCourse(Long userId, CourseRequestDto courseRequestDto) {
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

        return EnrollmentCourseDetailDto.builder()
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
    public EnrollmentCourseDetailDto readCourse(Long courseId) {
        // Course 존재유무 확인
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findByIdAndStatus(courseId, true).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));

        // Point to PointDto, Tag to TagDto 변환
        List<PointDto> locations = courseUtil.getPoint2PointDto(enrollmentCourse.getLocations());
        List<CourseTagDto> courseTagDtoList = courseUtil.getTag2TagDto(enrollmentCourse.getCourseTags());

        return EnrollmentCourseDetailDto.builder()
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

    public EnrollmentCourseDetailDto updateCourse(Long userId, Long courseId, CourseRequestDto courseRequestDto) {
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

        return EnrollmentCourseDetailDto.builder()
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

    // 나만의 Tap - 개인 산책로 조회용
    public List<IndividualCourseListDto> getIndividualCourseList(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<IndividualCourse> page =  individualCourseRepository.findListByUser(user, paging);

        List<IndividualCourseListDto> individualCourseListDtoList = new ArrayList<>();
        for (IndividualCourse course : page.getContent()) {
            individualCourseListDtoList.add(IndividualCourseListDto.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .createdDate(course.getCreatedDate())
                    .distance(course.getDistance()).build());
        }

        return individualCourseListDtoList;
    }

    // 나만의 Tap - 등록한 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByUser(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findListByUser(user, paging);

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseListDtos(user, page);

        return list;
    }

    // 나만의 Tap - 좋아요한 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByLikeAndUser(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findListByLikeAndUser(user, paging);

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseListDtos(user, page);
        return list;
    }

    // 나만의 Tap - 이용한 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByUsingAndUser(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findListByUsingAndUser(user, paging);

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseListDtos(user, page);
        return list;
    }

    // 나만의 Tap, Main Tap - 태그를 가진 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByTag(Long userId, Long pageNum, Long Num, String tag) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        // Tag 존재유무 확인
        CourseTagType courseTagType = CourseTagType.existType(tag);
        if (courseTagType == null) {
            throw new RestApiException(ErrorCode.NOT_FOUND_COURSE_TAG);
        }

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findListByTag(courseTagType, paging);

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseListDtos(user, page);

        return list;
    }

    // 산책로 Tap - 해당 유저의 성향에 따라 추천 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByRecommend(Long userId, Long pageNum, Long Num) {
        return null;
    }

    // 산책로 Tap - 최신순 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseList(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findAll(paging);

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseListDtos(user, page);

        return list;
    }

    // 산책로 Tap - 좋아요순 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByLikeCount(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findAll(paging);

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseListDtos(user, page);

        return list;
    }

    // 산책로 Tap - 이용자순 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByUsingCount(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findAll(paging);

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseListDtos(user, page);

        return list;
    }

    // 산책로 Tap, Main Tap - 거리순 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByLocation(Long userId, Long pageNum, Long Num, Double latitude, Double longitude) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.ASC, "radius"));
        Page<CourseMapping> pages =  enrollmentCourseRepository.findListByLocation(courseUtil.getLatLng2Point(latitude, longitude), paging);

        List<EnrollmentCourseListDto> enrollmentCourseListDtoList = new ArrayList<>();
        for (CourseMapping courseMapping : pages.getContent()) {
            EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(courseMapping.getId()).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COURSE));

            enrollmentCourseListDtoList.add(EnrollmentCourseListDto.builder()
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

        return enrollmentCourseListDtoList;
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