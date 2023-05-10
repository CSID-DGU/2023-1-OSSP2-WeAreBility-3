package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.CourseTag;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseTagRepository extends JpaRepository<CourseTag, Long> {
    public void deleteByCourseIdAndCourseTagType(Long courseId, CourseTagType courseTagType);

}
