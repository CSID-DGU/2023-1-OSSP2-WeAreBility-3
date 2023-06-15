package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.Comment;
import com.dongguk.cse.naemansan.domain.EnrollmentCourse;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.dto.response.CommentDto;
import com.dongguk.cse.naemansan.dto.request.CommentRequestDto;
import com.dongguk.cse.naemansan.event.CommentBadgeEvent;
import com.dongguk.cse.naemansan.event.CommentNotificationEvent;
import com.dongguk.cse.naemansan.repository.CommentRepository;
import com.dongguk.cse.naemansan.repository.EnrollmentCourseRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final UserRepository userRepository;
    private final EnrollmentCourseRepository enrollmentCourseRepository;
    private final CommentRepository commentRepository;

    private final ApplicationEventPublisher publisher;

    public Boolean createComment(Long userId, Long courseId, CommentRequestDto commentRequestDto) {
        // User, Course 존재유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(courseId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));

        // 댓글 추가
        commentRepository.save(Comment.builder()
                .user(user)
                .enrollmentCourse(enrollmentCourse)
                .content(commentRequestDto.getContent()).build());

        publisher.publishEvent(new CommentBadgeEvent(userId));
        publisher.publishEvent(new CommentNotificationEvent(userId, enrollmentCourse.getUser().getId(), courseId));
        return Boolean.TRUE;
    }

    public List<CommentDto> readComment(Long courseId, Long pageNum, Long num) {
        // Course 존재유무 확인
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(courseId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));

        Pageable paging = PageRequest.of(pageNum.intValue(), num.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Comment> comments = commentRepository.findByEnrollmentCourseAndStatus(enrollmentCourse, true, paging);

        // Dto 변환
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments.getContent()) {
            commentDtoList.add(CommentDto.builder()
                    .id(comment.getId())
                    .user_id(comment.getUser().getId())
                    .course_id(comment.getEnrollmentCourse().getId())
                    .user_name(comment.getUser().getName())
                    .content(comment.getContent())
                    .created_date(comment.getCreatedDate())
                    .is_edit(comment.getIsEdit()).build());
        }

        // Dto 반환
        return commentDtoList;
    }

    public Boolean updateComment(Long userId, Long courseId, Long commentId, CommentRequestDto commentRequestDto) {
        // User, Course, Comment 존재유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(courseId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));
        Comment comment = commentRepository.findByIdAndUserAndEnrollmentCourseAndStatus(commentId, user, enrollmentCourse, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COMMENT));

        // Comment 수정
        if ((commentRequestDto.getContent() == null) || (commentRequestDto.getContent().length() == 0)) {
            throw new RestApiException(ErrorCode.NOT_EXIST_PARAMETER);
        }

        comment.setContent(commentRequestDto.getContent());
        comment.setIsEdit(true);

        return Boolean.TRUE;
    }

    public Boolean deleteComment(Long userId, Long courseId, Long commentId) {
        // User, Course, Comment 존재유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(courseId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));
        Comment comment = commentRepository.findByIdAndUserAndEnrollmentCourseAndStatus(commentId, user, enrollmentCourse, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COMMENT));

        // 삭제 - status 변경
        comment.setStatus(false);
        return Boolean.TRUE;
    }
}
