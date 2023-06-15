package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Comment;
import com.dongguk.cse.naemansan.domain.CourseTag;
import com.dongguk.cse.naemansan.domain.EnrollmentCourse;
import com.dongguk.cse.naemansan.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByEnrollmentCourseAndStatus(EnrollmentCourse course, Boolean status, Pageable pageable);

    Optional<Comment> findByIdAndUserAndEnrollmentCourseAndStatus(Long id, User user, EnrollmentCourse enrollmentCourse, Boolean status);

    Long countByUser(User user);

    Long countByUserAndStatus(User user, Boolean status);

    Optional<Comment> findByIdAndUserAndEnrollmentCourse(Long id, User user, EnrollmentCourse enrollmentCourse);

    @Query(value = "SELECT c FROM Comment c WHERE c.user = :user AND c.status = true")
    Page<Comment> findListByUser(@Param("user") User user, Pageable pageable);
}
