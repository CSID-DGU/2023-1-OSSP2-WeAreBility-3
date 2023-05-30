package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.IndividualCourse;
import com.dongguk.cse.naemansan.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IndividualCourseRepository extends JpaRepository<IndividualCourse, Long> {
    Optional<IndividualCourse> findByUserAndTitleAndStatus(User user, String title, Boolean status);
    Optional<IndividualCourse> findByIdAndUserIdAndStatus(Long courseId, Long userId, Boolean status);
    @Query(value = "SELECT i FROM IndividualCourse i WHERE i.user = :user AND i.status = true")
    Page<IndividualCourse> findListByUser(@Param("user") User user, Pageable pageable);

    Long countByUser(User user);
}
