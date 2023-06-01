package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.request.UserTagRequestDto;
import com.dongguk.cse.naemansan.dto.response.CommentDto;
import com.dongguk.cse.naemansan.dto.response.EnrollmentCourseListDto;
import com.dongguk.cse.naemansan.dto.response.UserDto;
import com.dongguk.cse.naemansan.dto.request.UserRequestDto;
import com.dongguk.cse.naemansan.repository.*;
import com.dongguk.cse.naemansan.util.CourseUtil;
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

    public UserDto readUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        return UserDto.builder()
                .user(user)
                .image(user.getImage())
                .is_premium(user.getSubscribe() != null)
                .comment_cnt((long) user.getComments().size())
                .like_cnt((long) user.getLikes().size())
                .badge_cnt((long) user.getBadges().size())
                .following_cnt((long) user.getFollowings().size())
                .follower_cnt((long) user.getFollowers().size()).build();
    }

    @Transactional
    public UserDto updateUserProfile(Long userId, UserRequestDto userRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        user.updateUser(userRequestDto.getName(), userRequestDto.getInformation());

        return UserDto.builder()
                .user(user)
                .image(user.getImage())
                .is_premium(user.getSubscribe() != null)
                .comment_cnt((long) user.getComments().size())
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

        List<UserTag> userTags = new ArrayList<>();
        for (CourseTagDto tagDto : requestDto.getTags()) {
            switch (tagDto.getStatus()) {
                case NEW -> {
                    userTags.add(userTagRepository.save(UserTag.builder()
                            .user(user)
                            .tag(tagDto.getName()).build()));
                }
                case DELETE -> {
                    userTagRepository.deleteByUserAndTag(user, tagDto.getName()); }
                case DEFAULT -> {
                    userTags.add(UserTag.builder()
                            .user(user)
                            .tag(tagDto.getName()).build()); }
            }
        }

        return courseUtil.getTag2TagDtoForUser(userTags);
    }

    public List<CommentDto> readCommentList(Long userId, Long pageNum, Long num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Comment> comments = commentRepository.findListByUser(user, paging);

        List<CommentDto> commentDtoList = new ArrayList<>();

        for (Comment comment: comments.getContent()) {
            commentDtoList.add(CommentDto.builder()
                    .id(comment.getId())
                    .user_id(comment.getUser().getId())
                    .course_id(comment.getEnrollmentCourse().getId())
                    .user_name(comment.getUser().getName())
                    .content(comment.getContent())
                    .created_date(comment.getCreatedDate())
                    .is_edit(comment.getIsEdit()).build());
        }

        return commentDtoList;
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
                    .tags(courseUtil.getTag2TagDtoForEnrollmentCourse(enrollmentCourse.getCourseTags()))
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
                    .tags(courseUtil.getTag2TagDtoForEnrollmentCourse(enrollmentCourse.getCourseTags()))
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
                    .tags(courseUtil.getTag2TagDtoForEnrollmentCourse(enrollmentCourse.getCourseTags()))
                    .start_location_name(enrollmentCourse.getStartLocationName())
                    .distance(enrollmentCourse.getDistance())
                    .like_cnt((long) enrollmentCourse.getLikes().size())
                    .using_unt((long) enrollmentCourse.getUsingCourses().size())
                    .is_like(true).build());
        }

        return enrollmentCourseListDtoList;
    }
}
