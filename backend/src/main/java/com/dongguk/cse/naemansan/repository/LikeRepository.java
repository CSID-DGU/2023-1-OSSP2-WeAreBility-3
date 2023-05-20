package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.EnrollmentCourse;
import com.dongguk.cse.naemansan.domain.Like;
import com.dongguk.cse.naemansan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUserId(Long userId);
    List<Like> findByEnrollmentCourseId(Long enrollmentCourseId);
    Optional<Like> findByUserAndEnrollmentCourse(User user, EnrollmentCourse enrollmentCourse);
    void deleteByUserIdAndEnrollmentCourseId(Long userId, Long enrollmentCourseId);
}
