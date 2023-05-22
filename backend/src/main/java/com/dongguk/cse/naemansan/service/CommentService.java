package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.Comment;
import com.dongguk.cse.naemansan.domain.EnrollmentCourse;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.dto.response.CommentDto;
import com.dongguk.cse.naemansan.dto.request.CommentRequestDto;
import com.dongguk.cse.naemansan.repository.CommentRepository;
import com.dongguk.cse.naemansan.repository.EnrollmentCourseRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        return Boolean.TRUE;
    }

    public List<CommentDto> readComment(Long courseId) {
        // Course 존재유무 확인
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(courseId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));

        // Dto 변환
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment: enrollmentCourse.getComments()) {
            // 삭제된 댓글 제외
            if (!comment.getStatus()) {
                continue;
            }
            
            commentDtoList.add(CommentDto.builder()
                    .id(comment.getId())
                    .userId(comment.getUser().getId())
                    .courseId(comment.getEnrollmentCourse().getId())
                    .userName(comment.getUser().getName())
                    .content(comment.getContent())
                    .createdDateTime(comment.getCreatedDate())
                    .isEdit(comment.getIsEdit()).build());
        }

        // Dto 반환
        return commentDtoList;
    }

    public Boolean updateComment(Long userId, Long courseId, Long commentId, CommentRequestDto commentRequestDto) {
        // User, Course, Comment 존재유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(courseId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));
        Comment comment = commentRepository.findByIdAndUserAndEnrollmentCourse(commentId, user, enrollmentCourse).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COMMENT));

        // Comment 수정
        comment.setContent(commentRequestDto.getContent());
        return Boolean.TRUE;
    }

    public Boolean deleteComment(Long userId, Long courseId, Long commentId) {
        // User, Course, Comment 존재유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse enrollmentCourse = enrollmentCourseRepository.findById(courseId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));
        Comment comment = commentRepository.findByIdAndUserAndEnrollmentCourse(commentId, user, enrollmentCourse).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_COMMENT));

        // 삭제 - 추후 status 로 수정할 예정
        commentRepository.delete(comment);
        return Boolean.TRUE;
    }
}
