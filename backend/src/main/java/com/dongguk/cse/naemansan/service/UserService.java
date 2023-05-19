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
        try {
            userRepository.deleteById(id);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return Boolean.FALSE;
    }

    public List<CommentDto> readCommentList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        List<Comment> commentList = user.getComments();
        List<CommentDto> commentDtoList = new ArrayList<>();

        for (Comment comment: commentList) {
            commentDtoList.add(CommentDto.builder()
                    .id(comment.getId())
                    .userId(comment.getCommentUser().getId())
                    .courseId(comment.getCommentCourse().getId())
                    .userName(comment.getCommentUser().getName())
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
            Course course = like.getLikeCourse();
            courseListDtoList.add(CourseListDto.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .courseTags(courseUtil.getTag2TagDto(course.getCourseTags()))
                    .startLocationName(course.getStartLocationName())
                    .distance(course.getDistance())
                    .likeCnt((long) course.getLikes().size())
                    .usingCnt((long) course.getUsingCourses().size())
                    .isLike(true).build());
            }

        return courseListDtoList;
    }

    public List<CourseListDto> readEnrollmentCourseList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        List<Course> courseList = user.getCourses();

        List<CourseListDto> courseListDtoList = new ArrayList<>();
        for (Course course : courseList) {
            courseListDtoList.add(CourseListDto.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .courseTags(courseUtil.getTag2TagDto(course.getCourseTags()))
                    .startLocationName(course.getStartLocationName())
                    .distance(course.getDistance())
                    .likeCnt((long) course.getLikes().size())
                    .usingCnt((long) course.getUsingCourses().size())
                    .isLike(true).build());
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
            Course course = usingCourse.getCourse();

            courseListDtoList.add(CourseListDto.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .courseTags(courseUtil.getTag2TagDto(course.getCourseTags()))
                    .startLocationName(course.getStartLocationName())
                    .distance(course.getDistance())
                    .likeCnt((long) course.getLikes().size())
                    .usingCnt((long) course.getUsingCourses().size())
                    .isLike(true).build());
        }

        return courseListDtoList;
    }
}
