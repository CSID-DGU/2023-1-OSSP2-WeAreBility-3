package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.CourseTag;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseTagRepository extends JpaRepository<CourseTag, Long> {
    public void deleteByCourseIdAndCourseTagType(Long courseId, CourseTagType courseTagType);
    public List<CourseTag> findByCourseTagType(CourseTagType courseTagType);
    public List<CourseTag> findByCourseId(Long courseId);
}
