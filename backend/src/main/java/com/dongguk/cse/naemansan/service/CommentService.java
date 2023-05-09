package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Comment;
import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.dto.CommentDto;
import com.dongguk.cse.naemansan.dto.CommentRequestDto;
import com.dongguk.cse.naemansan.repository.CommentRepository;
import com.dongguk.cse.naemansan.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CourseRepository courseRepository;
    private final CommentRepository commentRepository;
    public Boolean createComment(Long userId, Long courseId, CommentRequestDto commentRequestDto) {
        Optional<Course> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            log.error("Not Exist course - CourseID : {}", courseId);
            return Boolean.FALSE;
        }

        // 댓글은 중복검사가 필요 없음
        commentRepository.save(Comment.builder()
                .userId(userId)
                .courseId(courseId)
                .context(commentRequestDto.getContent()).build());

        return Boolean.TRUE;
    }

    public List<CommentDto> readComment(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            log.error("Not Exist course - CourseID : {}", courseId);
            return null;
        }

        List<Comment> comments = commentRepository.findByCourseId(courseId);

        List<CommentDto> commentDtos = new ArrayList<>();

        for (Comment comment: comments) {
            commentDtos.add(CommentDto.builder()
                    .id(comment.getId())
                    .userId(comment.getUserId())
                    .content(comment.getContext())
                    .createdDateTime(comment.getCreatedDate())
                    .isEdit(comment.getIsEdit()).build());
        }

        return commentDtos;
    }

    public Boolean updateComment(Long userId, Long courseId, Long commentId, CommentRequestDto commentRequestDto) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            log.error("Not Exist course - CourseID : {}", courseId);
            return Boolean.FALSE;
        }

        Optional<Comment> comment = commentRepository.findByUserIdAndCourseIdAndId(userId, courseId, commentId);

        if (comment.isEmpty()) {
            log.error("Not Exist Comment - UserID : {}, CourseId : {}, CommentId : {}", userId, courseId, commentId);
            return Boolean.FALSE;
        }

        comment.get().setContext(commentRequestDto.getContent());
        return Boolean.TRUE;
    }

    public Boolean deleteComment(Long userId, Long courseId, Long commentId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            log.error("Not Exist course - CourseID : {}", courseId);
            return Boolean.FALSE;
        }

        Optional<Comment> comment = commentRepository.findByUserIdAndCourseIdAndId(userId, courseId, commentId);

        if (comment.isEmpty()) {
            log.error("Not Exist Comment - UserID : {}, CourseId : {}, CommentId : {}", userId, courseId, commentId);
            return Boolean.FALSE;
        }

        // status로 수정할 예정
        commentRepository.deleteById(commentId);
        return Boolean.TRUE;
    }
}
