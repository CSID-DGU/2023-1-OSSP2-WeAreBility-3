package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import com.dongguk.cse.naemansan.dto.request.IndividualCourseRequestDto;
import com.dongguk.cse.naemansan.dto.response.*;
import com.dongguk.cse.naemansan.dto.request.EnrollmentCourseRequestDto;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import com.dongguk.cse.naemansan.event.EnrollmentCourseBadgeEvent;
import com.dongguk.cse.naemansan.event.IndividualCourseBadgeEvent;
import com.dongguk.cse.naemansan.event.LikeNotificationEvent;
import com.dongguk.cse.naemansan.event.UsingCourseBadgeEvent;
import com.dongguk.cse.naemansan.repository.*;
import com.dongguk.cse.naemansan.util.CourseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.context.ApplicationEventPublisher;
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
    private final IndividualCourseRepository individualCourseRepository;
    private final EnrollmentCourseRepository enrollmentCourseRepository;
    private final UsingCourseRepository usingCourseRepository;
    private final CourseTagRepository courseTagRepository;
    private final LikeRepository likeRepository;
    private final CourseUtil courseUtil;

    private final ApplicationEventPublisher publisher;

    // Individual Course Create
    public IndividualCourseDetailDto createIndividualCourse(Long userId, IndividualCourseRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        individualCourseRepository.findByUserAndTitleAndStatus(user, requestDto.getTitle(), true).ifPresent(str -> { throw new RestApiException(ErrorCode.DUPLICATION_TITLE); });

        Map<String, Object> pointInformation = courseUtil.getPointDto2Point(requestDto.getLocations());
        MultiPoint multiPoint = (MultiPoint) pointInformation.get("locations");
        double distance = (double) pointInformation.get("distance");

        IndividualCourse course = individualCourseRepository.save(IndividualCourse.builder()
                .user(user)
                .title(requestDto.getTitle())
                .locations(multiPoint)
                .distance(distance).build());

        publisher.publishEvent(new IndividualCourseBadgeEvent(userId));

        return IndividualCourseDetailDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .locations(requestDto.getLocations())
                .create_date(course.getCreatedDate())
                .distance(course.getDistance()).build();
    }

    // Individual Course Read
    public IndividualCourseDetailDto readIndividualCourse(Long userId, Long courseId) {
        // 해당 유저가 만든 Course 존재 유무 확인
        IndividualCourse course = individualCourseRepository.findByIdAndUserIdAndStatus(courseId, userId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_INDIVIDUAL_COURSE));

        return IndividualCourseDetailDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .locations(courseUtil.getPoint2PointDto(course.getLocations()))
                .create_date(course.getCreatedDate())
                .distance(course.getDistance()).build();
    }

    // Individual Course Update - 사용 X
    public Boolean updateIndividualCourse(Long userId, Long courseId) {
        // 해당 유저가 만든 Course 존재 유무 확인
        IndividualCourse course = individualCourseRepository.findByIdAndUserIdAndStatus(courseId, userId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_INDIVIDUAL_COURSE));

//        individualCourseRepository.delete(course);
        return Boolean.TRUE;
    }

    // Individual Course Delete
    public Boolean deleteIndividualCourse(Long userId, Long courseId) {
        // 해당 유저가 만든 Course 존재 유무 확인
        IndividualCourse course = individualCourseRepository.findByIdAndUserIdAndStatus(courseId, userId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_INDIVIDUAL_COURSE));

        course.setStatus(false);
        return Boolean.TRUE;
    }

    public Boolean createUsingCourse(Long userId, UsingCourseRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse =  enrollmentCourseRepository.findByIdAndStatus(requestDto.getEnrollment_id(),true)
                .orElseThrow(()-> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));

        publisher.publishEvent(new UsingCourseBadgeEvent(userId));

        Boolean finishState = courseUtil.checkFinishState(requestDto.getEnrollment_id(), requestDto.getLocations());
        usingCourseRepository.save(UsingCourse.builder()
                .user(user)
                .enrollmentCourse(enrollmentCourse)
                .finishStatus(finishState).build());

        return Boolean.TRUE;
    }

    // Course Create
    public EnrollmentCourseDetailDto createEnrollmentCourse(Long userId, EnrollmentCourseRequestDto requestDto) {
        // User 존재, , Course Title 중복유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        IndividualCourse individualCourse = individualCourseRepository.findByIdAndUserIdAndStatus(requestDto.getIndividual_id(), userId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_INDIVIDUAL_COURSE));
        enrollmentCourseRepository.findByTitleAndStatus(requestDto.getTitle(), true).ifPresent(s ->{ throw new RestApiException(ErrorCode.DUPLICATION_TITLE); });

        List<PointDto> pointDtoList = courseUtil.getPoint2PointDto(individualCourse.getLocations());
        if (!courseUtil.checkCourse(pointDtoList)) {
            throw new RestApiException(ErrorCode.DUPLICATION_LOCATIONS);
        }

        MultiPoint multiPoint = individualCourse.getLocations();
        Point point = courseUtil.getStartLocation(multiPoint.getGeometryN(0).getCoordinate());
        double distance = (double) individualCourse.getDistance();

        // Course DB 등록
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.save(EnrollmentCourse.builder()
                .user(user)
                .title(requestDto.getTitle())
                .introduction(requestDto.getIntroduction())
                .startLocationName(courseUtil.getLocationName(courseUtil.getPoint2PointDto(point)) == null ? "임시시작" : courseUtil.getLocationName(courseUtil.getPoint2PointDto(point)))
                .startLocation(point)
                .locations(multiPoint)
                .distance(distance).build());

        // CourseTag 등록하는 과정(TagDto2Tag and saveAll)
        List<CourseTag> courseTags = courseUtil.getTagDto2TagForEnrollmentCourse(enrollmentCourse, requestDto.getTags());
        courseTagRepository.saveAll(courseTags);

        publisher.publishEvent(new EnrollmentCourseBadgeEvent(userId));

        // ResponseDto 를 위한 TagDto 생성
        List<CourseTagDto> courseTagDtoList = courseUtil.getTag2TagDtoForCourse(courseTags);

        return EnrollmentCourseDetailDto.builder()
                .id(enrollmentCourse.getId())
                .user_id(enrollmentCourse.getUser().getId())
                .user_name(enrollmentCourse.getUser().getName())
                .title(enrollmentCourse.getTitle())
                .created_date(enrollmentCourse.getCreatedDate())
                .introduction(enrollmentCourse.getIntroduction())
                .tags(courseTagDtoList)
                .start_location_name(enrollmentCourse.getStartLocationName())
                .locations(courseUtil.getPoint2PointDto(enrollmentCourse.getLocations()))
                .distance(enrollmentCourse.getDistance())
                .is_like(false).build();
    }

    // Course Read
    public EnrollmentCourseDetailDto readEnrollmentCourse(Long userId, Long courseId) {
        // Course 존재유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findByIdAndStatus(courseId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));

        // Point to PointDto, Tag to TagDto 변환
        List<PointDto> locations = courseUtil.getPoint2PointDto(enrollmentCourse.getLocations());
        List<CourseTagDto> courseTagDtoList = courseUtil.getTag2TagDtoForCourse(enrollmentCourse.getCourseTags());
        return EnrollmentCourseDetailDto.builder()
                .id(enrollmentCourse.getId())
                .user_id(enrollmentCourse.getUser().getId())
                .user_name(enrollmentCourse.getUser().getName())
                .title(enrollmentCourse.getTitle())
                .created_date(enrollmentCourse.getCreatedDate())
                .introduction(enrollmentCourse.getIntroduction())
                .tags(courseTagDtoList)
                .start_location_name(enrollmentCourse.getStartLocationName())
                .locations(locations)
                .distance(enrollmentCourse.getDistance())
                .like_cnt((long) enrollmentCourse.getLikes().size())
                .is_like(courseUtil.existLike(user, enrollmentCourse)).build();
    }

    public EnrollmentCourseDetailDto updateEnrollmentCourse(Long userId, Long courseId, EnrollmentCourseRequestDto requestDto) {
        // User, Course 존재유무, Course Title 중복유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findByIdAndStatus(courseId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));
        enrollmentCourseRepository.findByIdNotAndTitleAndStatus(courseId, requestDto.getTitle(), true)
                .ifPresent(c -> { throw new RestApiException(ErrorCode.DUPLICATION_TITLE);});

        // Course User 와 Request User 동등유무 확인
        if (enrollmentCourse.getUser().getId() != user.getId()) {
            throw new RestApiException(ErrorCode.NOT_EQUAL);
        }

        // Comment 수정
        if ((requestDto.getTitle() == null) || (requestDto.getTitle().length() == 0)) {
            throw new RestApiException(ErrorCode.NOT_EXIST_PARAMETER);
        }

        // Course Data Update
        enrollmentCourse.updateCourse(requestDto.getTitle(), requestDto.getIntroduction());

        // Course Tag Data Update, 최적화 필요
        courseTagRepository.deleteAll(enrollmentCourse.getCourseTags());

        List<CourseTag> courseTags = courseTagRepository.saveAll(courseUtil.getTagDto2TagForEnrollmentCourse(enrollmentCourse, requestDto.getTags()));
//        List<CourseTag> courseTagList = new ArrayList<>();
//        for (CourseTagDto courseTagDto : enrollmentCourseRequestDto.getTags()) {
//            switch (courseTagDto.getStatus()) {
//                case NEW -> {
//                    courseTagList.add(courseTagRepository.save(CourseTag.builder()
//                            .enrollmentCourse(enrollmentCourse)
//                            .courseTagType(courseTagDto.getName()).build()));
//                }
//                case DELETE -> { courseTagRepository.deleteByEnrollmentCourseAndCourseTagType(enrollmentCourse, courseTagDto.getName()); }
//                case DEFAULT -> { courseTagList.add(CourseTag.builder()
//                        .enrollmentCourse(enrollmentCourse)
//                        .courseTagType(courseTagDto.getName()).build()); }
//            }
//        }

        // ResponseDto 를 위한 PointDto 생성
        List<PointDto> locations = courseUtil.getPoint2PointDto(enrollmentCourse.getLocations());

        return EnrollmentCourseDetailDto.builder()
                .id(enrollmentCourse.getId())
                .user_id(enrollmentCourse.getUser().getId())
                .user_name(enrollmentCourse.getUser().getName())
                .title(enrollmentCourse.getTitle())
                .created_date(enrollmentCourse.getCreatedDate())
                .introduction(enrollmentCourse.getIntroduction())
                .tags(courseUtil.getTag2TagDtoForCourse(courseTags))
                .start_location_name(enrollmentCourse.getStartLocationName())
                .locations(locations)
                .distance(enrollmentCourse.getDistance())
                .is_like(courseUtil.existLike(user, enrollmentCourse)).build();
    }

    public Boolean deleteEnrollmentCourse(Long userId, Long courseId) {
        // User 존재, Course 존재 유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findByIdAndStatus(courseId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));

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
                    .created_date(course.getCreatedDate())
                    .distance(course.getDistance()).build());
        }

        return individualCourseListDtoList;
    }

    // 나만의 Tap - 등록한 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByUser(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findListByUser(user, paging);

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseList(user, page);

        return list;
    }

    // 나만의 Tap - 좋아요한 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByLikeAndUser(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findListByLikeAndUser(user, paging);

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseList(user, page);
        return list;
    }

    // 나만의 Tap - 이용한 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByUsingAndUser(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findListByUsingAndUser(user, paging);

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseList(user, page);
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

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseList(user, page);

        return list;
    }

    // 산책로 Tap - 해당 유저의 성향에 따라 추천 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByRecommend(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        List<Long> recommends = courseUtil.getRecommendList(userId);

        if (recommends.size() == 0) {
            return new ArrayList<>();
        }


        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue());
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findListByRecommend(recommends, paging);
        log.info("{} - {}", user.getId(), recommends);
        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseList(user, page);

        return list;
    }

    // 산책로 Tap - 최신순 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseList(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<EnrollmentCourse> page =  enrollmentCourseRepository.findListAll(paging);

        List<EnrollmentCourseListDto> list = courseUtil.getEnrollmentCourseList(user, page);

        return list;
    }

    // 산책로 Tap - 좋아요순 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByLikeCount(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue());
        Page<EnrollmentCourseRepository.CourseCntForm> page =  enrollmentCourseRepository.findListByLike(paging);

        List<EnrollmentCourseListDto> list = new ArrayList<>();
        for (EnrollmentCourseRepository.CourseCntForm form : page.getContent()) {
            EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(form.getId())
                    .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));

            list.add(EnrollmentCourseListDto.builder()
                    .id(enrollmentCourse.getId())
                    .title(enrollmentCourse.getTitle())
                    .created_date(enrollmentCourse.getCreatedDate())
                    .tags(courseUtil.getTag2TagDtoForCourse(enrollmentCourse.getCourseTags()))
                    .start_location_name(enrollmentCourse.getStartLocationName())
                    .distance(enrollmentCourse.getDistance())
                    .like_cnt((long) enrollmentCourse.getLikes().size())
                    .using_unt((long) enrollmentCourse.getUsingCourses().size())
                    .is_like(courseUtil.existLike(user, enrollmentCourse)).build());
        }

        return list;
    }

    // 산책로 Tap - 이용자순 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByUsingCount(Long userId, Long pageNum, Long Num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue());
        Page<EnrollmentCourseRepository.CourseCntForm> page =  enrollmentCourseRepository.findListByUsing(paging);

        List<EnrollmentCourseListDto> list = new ArrayList<>();
        for (EnrollmentCourseRepository.CourseCntForm form : page.getContent()) {
            EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(form.getId())
                    .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));

            list.add(EnrollmentCourseListDto.builder()
                    .id(enrollmentCourse.getId())
                    .title(enrollmentCourse.getTitle())
                    .created_date(enrollmentCourse.getCreatedDate())
                    .tags(courseUtil.getTag2TagDtoForCourse(enrollmentCourse.getCourseTags()))
                    .start_location_name(enrollmentCourse.getStartLocationName())
                    .distance(enrollmentCourse.getDistance())
                    .like_cnt((long) enrollmentCourse.getLikes().size())
                    .using_unt((long) enrollmentCourse.getUsingCourses().size())
                    .is_like(courseUtil.existLike(user, enrollmentCourse)).build());
        }

        return list;
    }

    // 산책로 Tap, Main Tap - 거리순 산책로 조회용
    public List<EnrollmentCourseListDto> getEnrollmentCourseListByLocation(Long userId, Long pageNum, Long Num, Double latitude, Double longitude) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.ASC, "radius"));
        Page<EnrollmentCourseRepository.CourseLocationForm> pages =  enrollmentCourseRepository.findListByLocation(courseUtil.getLatLng2Point(latitude, longitude), paging);

        List<EnrollmentCourseListDto> enrollmentCourseListDtoList = new ArrayList<>();
        for (EnrollmentCourseRepository.CourseLocationForm form : pages.getContent()) {
            EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(form.getId())
                    .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));

            enrollmentCourseListDtoList.add(EnrollmentCourseListDto.builder()
                    .id(enrollmentCourse.getId())
                    .title(enrollmentCourse.getTitle())
                    .created_date(enrollmentCourse.getCreatedDate())
                    .tags(courseUtil.getTag2TagDtoForCourse(enrollmentCourse.getCourseTags()))
                    .start_location_name(enrollmentCourse.getStartLocationName())
                    .distance(enrollmentCourse.getDistance())
                    .like_cnt((long) enrollmentCourse.getLikes().size())
                    .using_unt((long) enrollmentCourse.getUsingCourses().size())
                    .is_like(courseUtil.existLike(user, enrollmentCourse)).build());
        }

        return enrollmentCourseListDtoList;
    }

    public Map<String, Object> likeCourse(Long userId, Long courseId) {
        // User, Course 존재, 이미 Like 했는지 여부 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findByIdAndStatus(courseId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));
        likeRepository.findByUserAndEnrollmentCourse(user, enrollmentCourse).ifPresent(i -> { throw new RestApiException(ErrorCode.EXIST_ENTITY_REQUEST); });

        // Like 저장
        likeRepository.save(Like.builder()
                .user(user)
                .enrollmentCourse(enrollmentCourse).build());
        publisher.publishEvent(new LikeNotificationEvent(userId,enrollmentCourse.getUser().getId(),courseId));
        Map<String, Object> map = new HashMap<>();
        map.put("like_cnt", enrollmentCourse.getLikes().size());
        map.put("is_like", Boolean.TRUE);

        return map;
    }

    public Map<String, Object> dislikeCourse(Long userId, Long courseId) {
        // User, Course 존재, Like 하지 않았는지 여부 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findByIdAndStatus(courseId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));
        Like like = likeRepository.findByUserAndEnrollmentCourse(user, enrollmentCourse).orElseThrow(() -> new RestApiException(ErrorCode.NOT_EXIST_ENTITY_REQUEST));

        // Like 삭제
        likeRepository.delete(like);

        Map<String, Object> map = new HashMap<>();
        map.put("like_cnt", enrollmentCourse.getLikes().size());
        map.put("is_like", Boolean.FALSE);

        return map;
    }

    public List<CourseTagType> getTagList() {
        return List.of(CourseTagType.values());
    }
}