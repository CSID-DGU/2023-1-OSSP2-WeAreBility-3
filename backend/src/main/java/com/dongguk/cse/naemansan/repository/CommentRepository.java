package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Comment;
import com.dongguk.cse.naemansan.domain.EnrollmentCourse;
import com.dongguk.cse.naemansan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(Long userId);
    List<Comment> findByEnrollmentCourseId(Long enrollmentCourseId);

    Optional<Comment> findByIdAndUserAndEnrollmentCourse(Long id, User user, EnrollmentCourse enrollmentCourse);
}
