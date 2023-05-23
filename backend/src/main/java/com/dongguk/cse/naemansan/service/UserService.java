package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.dto.response.CommentDto;
import com.dongguk.cse.naemansan.dto.response.CourseListDto;
import com.dongguk.cse.naemansan.dto.response.UserDto;
import com.dongguk.cse.naemansan.dto.request.UserRequestDto;
import com.dongguk.cse.naemansan.repository.*;
import com.dongguk.cse.naemansan.util.CourseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final CourseUtil courseUtil;

    public UserDto readUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        return UserDto.builder()
                .user(user)
                .image(user.getImage())
                .isPremium(user.getSubscribe() != null)
                .commentCnt((long) user.getComments().size())
                .likeCnt((long) user.getLikes().size())
                .badgeCnt((long) user.getBadges().size())
                .followingCnt((long) user.getFollowings().size())
                .followerCnt((long) user.getFollowers().size())
                .build();
    }

    @Transactional
    public UserDto updateUserProfile(Long userId, UserRequestDto userRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        user.updateUser(userRequestDto.getName(), userRequestDto.getInformation());

        return UserDto.builder()
                .user(user)
                .image(user.getImage())
                .isPremium(user.getSubscribe() != null)
                .commentCnt((long) user.getComments().size())
                .likeCnt((long) user.getLikes().size())
                .badgeCnt((long) user.getBadges().size())
                .followingCnt((long) user.getFollowings().size())
                .followerCnt((long) user.getFollowers().size())
                .build();
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

    public List<CommentDto> readCommentList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        List<Comment> commentList = user.getComments();
        List<CommentDto> commentDtoList = new ArrayList<>();

        for (Comment comment: commentList) {
            commentDtoList.add(CommentDto.builder()
                    .id(comment.getId())
                    .userId(comment.getUser().getId())
                    .courseId(comment.getEnrollmentCourse().getId())
                    .userName(comment.getUser().getName())
                    .content(comment.getContent())
                    .createdDateTime(comment.getCreatedDate())
                    .isEdit(comment.getIsEdit()).build());
        }

        return commentDtoList;
    }

    public List<CourseListDto> readLikeCourseList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        List<Like> likeList = user.getLikes();

        List<CourseListDto> courseListDtoList = new ArrayList<>();
        for (Like like : likeList) {
            EnrollmentCourse enrollmentCourse = like.getEnrollmentCourse();
            courseListDtoList.add(CourseListDto.builder()
                    .id(enrollmentCourse.getId())
                    .title(enrollmentCourse.getTitle())
                    .createdDateTime(enrollmentCourse.getCreatedDate())
                    .courseTags(courseUtil.getTag2TagDto(enrollmentCourse.getCourseTags()))
                    .startLocationName(enrollmentCourse.getStartLocationName())
                    .distance(enrollmentCourse.getDistance())
                    .likeCnt((long) enrollmentCourse.getLikes().size())
                    .usingCnt((long) enrollmentCourse.getUsingCourses().size())
                    .isLike(true).build());
            }

        return courseListDtoList;
    }

    public List<CourseListDto> readEnrollmentCourseList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        List<EnrollmentCourse> enrollmentCourseList = user.getEnrollmentCourses();

        List<CourseListDto> courseListDtoList = new ArrayList<>();
        for (EnrollmentCourse enrollmentCourse : enrollmentCourseList) {
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

    public List<CourseListDto> readFinishCourseList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        List<UsingCourse> usingCourseList = user.getUsingCourses();

        List<CourseListDto> courseListDtoList = new ArrayList<>();
        for (UsingCourse usingCourse : usingCourseList) {
            if (!usingCourse.getFinishStatus()) {
                continue;
            }
            EnrollmentCourse enrollmentCourse = usingCourse.getEnrollmentCourse();

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
}
