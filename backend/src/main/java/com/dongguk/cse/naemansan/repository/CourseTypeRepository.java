package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.CourseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseTypeRepository extends JpaRepository<CourseTag, Long> {
}
