package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.EnrollmentCourse;
import com.dongguk.cse.naemansan.domain.type.CourseMapping;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentCourseRepository extends JpaRepository<EnrollmentCourse, Long> {
    Optional<EnrollmentCourse> findByIdNotAndTitleAndStatus(Long courseId, String title, Boolean status);
    Optional<EnrollmentCourse> findByIdAndStatus(Long courseId, Boolean Status);
    //    @Query(value = "SELECT c.title FROM EnrollmentCourse c WHERE c.title = :title AND c.status = :status")
    Optional<EnrollmentCourse> findByTitleAndStatus(String title, Boolean status);

    @Query(value = "SELECT c.id AS id, c.title AS title, c.created_date AS created_date, c.start_location_name AS start_location_name,"
            + "c.distance AS distance, ST_Distance_Sphere(:start, c.start_location) AS radius "
            + "FROM enrollment_courses c "
            + "WHERE ST_Distance_Sphere(:start, c.start_location) <= 1500 AND c.status = 1 ORDER BY radius LIMIT :limitNum", nativeQuery = true)
    List<CourseMapping> findCourseListByLocation(@Param("start") Point point, @Param("limitNum") Long limitNum);

    @Query(value = "SELECT c FROM EnrollmentCourse c LEFT JOIN CourseTag t WHERE t.courseTagType = :tag ORDER BY c.createdDate DESC")
    Page<EnrollmentCourse> findCourseListByTag(@Param("tag") CourseTagType courseTagType, Pageable paging);

//    @Query(value = "SELECT c.title FROM EnrollmentCourse c WHERE c.title = :title AND c.status = :status")
//    Optional<EnrollmentCourse> findByTitleAndStatus(@Param("title") String title, @Param("status") Boolean status);
}