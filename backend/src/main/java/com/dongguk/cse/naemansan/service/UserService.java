package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.request.UserDeviceRequestDto;
import com.dongguk.cse.naemansan.dto.request.UserPaymentRequestDto;
import com.dongguk.cse.naemansan.dto.request.UserTagRequestDto;
import com.dongguk.cse.naemansan.dto.response.CommentListDto;
import com.dongguk.cse.naemansan.dto.response.EnrollmentCourseListDto;
import com.dongguk.cse.naemansan.dto.response.UserDto;
import com.dongguk.cse.naemansan.dto.request.UserRequestDto;
import com.dongguk.cse.naemansan.repository.*;
import com.dongguk.cse.naemansan.util.CourseUtil;
import com.dongguk.cse.naemansan.util.PaymentUtil;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTagRepository userTagRepository;
    private final CommentRepository commentRepository;
    private final CourseUtil courseUtil;
    private final PaymentUtil paymentUtil;

    public UserDto readUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        Long commentCnt = commentRepository.countByUserAndStatus(user, true);

        return UserDto.builder()
                .user(user)
                .image(user.getImage())
                .is_premium(user.getIsPremium())
                .comment_cnt(commentCnt)
                .like_cnt((long) user.getLikes().size())
                .badge_cnt((long) user.getBadges().size())
                .following_cnt((long) user.getFollowings().size())
                .follower_cnt((long) user.getFollowers().size()).build();
    }

    @Transactional
    public UserDto updateUserProfile(Long userId, UserRequestDto userRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        userRepository.findByIdNotAndName(userId, userRequestDto.getName()).ifPresent(u -> { throw new RestApiException(ErrorCode.DUPLICATION_NAME); });

        if ((userRequestDto.getName() == null) || (userRequestDto.getName().length() == 0)) {
            throw new RestApiException(ErrorCode.NOT_EXIST_PARAMETER);
        }

        user.updateUser(userRequestDto.getName(), userRequestDto.getIntroduction());
        Long commentCnt = commentRepository.countByUserAndStatus(user, true);

        return UserDto.builder()
                .user(user)
                .image(user.getImage())
                .is_premium(user.getIsPremium())
                .comment_cnt(commentCnt)
                .like_cnt((long) user.getLikes().size())
                .badge_cnt((long) user.getBadges().size())
                .following_cnt((long) user.getFollowings().size())
                .follower_cnt((long) user.getFollowers().size()).build();
    }

    public Boolean deleteUserProfile(Long id) {
        // 삭제할 유저를 찾고, 해당 유저가 작성한 course, comment 를 Super_Admin(삭제된 게시물 용) 계정으로 Update
        User user = userRepository.findById(id).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        User Admin = userRepository.findById(1l).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        List<EnrollmentCourse> enrollmentCourseList = user.getEnrollmentCourses();
        List<Comment> comments =  user.getComments();
        userRepository.delete(user);

        for (EnrollmentCourse enrollmentCourse : enrollmentCourseList) {
            enrollmentCourse.setUser(Admin);
        }

        for (Comment comment : comments) {
            comment.setUser(Admin);
        }

        return Boolean.TRUE;
    }

    public List<CourseTagDto> createTagByUserChoice(Long userId, UserTagRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        List<UserTag> userTags = courseUtil.getTagDto2TagForUser(user, requestDto.getTags());
        userTagRepository.saveAll(userTags);

        return courseUtil.getTag2TagDtoForUser(userTags);
    }

    public List<CourseTagDto> readTagByUserChoice(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        List<UserTag> userTags = userTagRepository.findByUser(user);

        return courseUtil.getTag2TagDtoForUser(userTags);
    }

    public List<CourseTagDto> updateTagByUserChoice(Long userId, UserTagRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        userTagRepository.deleteAll(user.getUserTags());

        List<UserTag> userTags = userTagRepository.saveAll(courseUtil.getTagDto2TagForUser(user, requestDto.getTags()));
//        List<UserTag> userTags = new ArrayList<>();
//        for (CourseTagDto tagDto : requestDto.getTags()) {
//            switch (tagDto.getStatus()) {
//                case NEW -> {
//                    userTags.add(userTagRepository.save(UserTag.builder()
//                            .user(user)
//                            .tag(tagDto.getName()).build()));
//                }
//                case DELETE -> {
//                    userTagRepository.deleteByUserAndTag(user, tagDto.getName()); }
//                case DEFAULT -> {
//                    userTags.add(UserTag.builder()
//                            .user(user)
//                            .tag(tagDto.getName()).build()); }
//            }
//        }

        return courseUtil.getTag2TagDtoForUser(userTags);
    }

    public List<CommentListDto> readCommentList(Long userId, Long pageNum, Long num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Comment> comments = commentRepository.findListByUser(user, paging);

        List<CommentListDto> list = new ArrayList<>();

        for (Comment comment: comments.getContent()) {
            EnrollmentCourse course = comment.getEnrollmentCourse();
            list.add(CommentListDto.builder()
                    .id(comment.getId())
                    .course_id(course.getId())
                    .course_title(course.getTitle())
                    .content(comment.getContent())
                    .tags(courseUtil.getTag2TagDtoForCourse(course.getCourseTags().subList(0, 2))).build());
        }

        return list;
    }

    public List<EnrollmentCourseListDto> readLikeCourseList(Long userId, Long pageNum, Long num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        List<Like> likeList = user.getLikes();

        List<EnrollmentCourseListDto> enrollmentCourseListDtoList = new ArrayList<>();
        for (Like like : likeList) {
            EnrollmentCourse enrollmentCourse = like.getEnrollmentCourse();
            enrollmentCourseListDtoList.add(EnrollmentCourseListDto.builder()
                    .id(enrollmentCourse.getId())
                    .title(enrollmentCourse.getTitle())
                    .created_date(enrollmentCourse.getCreatedDate())
                    .tags(courseUtil.getTag2TagDtoForCourse(enrollmentCourse.getCourseTags()))
                    .start_location_name(enrollmentCourse.getStartLocationName())
                    .distance(enrollmentCourse.getDistance())
                    .like_cnt((long) enrollmentCourse.getLikes().size())
                    .using_unt((long) enrollmentCourse.getUsingCourses().size())
                    .is_like(true).build());
            }

        return enrollmentCourseListDtoList;
    }

    public List<EnrollmentCourseListDto> readEnrollmentCourseList(Long userId, Long pageNum, Long num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        List<EnrollmentCourse> enrollmentCourseList = user.getEnrollmentCourses();

        List<EnrollmentCourseListDto> enrollmentCourseListDtoList = new ArrayList<>();
        for (EnrollmentCourse enrollmentCourse : enrollmentCourseList) {
            enrollmentCourseListDtoList.add(EnrollmentCourseListDto.builder()
                    .id(enrollmentCourse.getId())
                    .title(enrollmentCourse.getTitle())
                    .created_date(enrollmentCourse.getCreatedDate())
                    .tags(courseUtil.getTag2TagDtoForCourse(enrollmentCourse.getCourseTags()))
                    .start_location_name(enrollmentCourse.getStartLocationName())
                    .distance(enrollmentCourse.getDistance())
                    .like_cnt((long) enrollmentCourse.getLikes().size())
                    .using_unt((long) enrollmentCourse.getUsingCourses().size())
                    .is_like(true).build());
        }

        return enrollmentCourseListDtoList;
    }

    public List<EnrollmentCourseListDto> readFinishCourseList(Long userId, Long pageNum, Long num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        List<UsingCourse> usingCourseList = user.getUsingCourses();

        List<EnrollmentCourseListDto> enrollmentCourseListDtoList = new ArrayList<>();
        for (UsingCourse usingCourse : usingCourseList) {
            if (!usingCourse.getFinishStatus()) {
                continue;
            }
            EnrollmentCourse enrollmentCourse = usingCourse.getEnrollmentCourse();

            enrollmentCourseListDtoList.add(EnrollmentCourseListDto.builder()
                    .id(enrollmentCourse.getId())
                    .title(enrollmentCourse.getTitle())
                    .created_date(enrollmentCourse.getCreatedDate())
                    .tags(courseUtil.getTag2TagDtoForCourse(enrollmentCourse.getCourseTags()))
                    .start_location_name(enrollmentCourse.getStartLocationName())
                    .distance(enrollmentCourse.getDistance())
                    .like_cnt((long) enrollmentCourse.getLikes().size())
                    .using_unt((long) enrollmentCourse.getUsingCourses().size())
                    .is_like(true).build());
        }

        return enrollmentCourseListDtoList;
    }

    public Boolean updateUserDevice(Long userId, UserDeviceRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        user.updateDevice(requestDto.getDevice_token(), requestDto.getIs_ios());
        return Boolean.TRUE;
    }

    public Boolean updatePremium(Long userId, UserPaymentRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        IamportResponse<Payment> irsp = null;
        try {
            irsp = paymentUtil.paymentLookup(requestDto.getImp_uid());
        } catch (Exception e) {
            throw new RestApiException(ErrorCode.PAYMENT_FAIL);
        }

        if (!paymentUtil.verifyIamport(irsp, requestDto.getAmount())) {
            throw new RestApiException(ErrorCode.PAYMENT_FAIL);
        }

        user.updatePremium(requestDto.getAmount() / 4900);

        return Boolean.TRUE;
    }
}
