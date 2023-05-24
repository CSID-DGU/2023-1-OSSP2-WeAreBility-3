package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.EnrollmentCourse;
import com.dongguk.cse.naemansan.domain.CourseTag;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseTagRepository extends JpaRepository<CourseTag, Long> {
    public void deleteByEnrollmentCourseAndCourseTagType(EnrollmentCourse enrollmentCourse, CourseTagType courseTagType);
    public List<CourseTag> findByCourseTagType(CourseTagType courseTagType);
    @Query(value = "SELECT c FROM CourseTag c WHERE c.enrollmentCourse.id = :courseId")
    public List<CourseTag> getCourseTags(@Param("courseId") Long courseId);
}
