package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.domain.UsingCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsingCourseRepository extends JpaRepository<UsingCourse, Long> {
    Long countByUser(User user);
}