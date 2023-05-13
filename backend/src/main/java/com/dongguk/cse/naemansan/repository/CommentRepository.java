package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Comment;
import com.dongguk.cse.naemansan.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(Long id);
    List<Comment> findByCourseId(Long courseId);

    Optional<Comment> findByUserIdAndCourseIdAndId(Long userId, Long courseId, Long id);
}
