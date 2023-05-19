package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.domain.type.CourseMapping;
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
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByIdNotAndTitle(Long courseId, String title);

    @Query(value = "SELECT course.id AS id, course.title AS title, course.created_date AS created_date, course.start_location_name AS start_location_name,"
            + "course.distance AS distance, ST_Distance_Sphere(:start, course.start_location) AS radius "
            + "FROM courses course "
            + "WHERE ST_Distance_Sphere(:start, course.start_location) <= 1500 ORDER BY radius LIMIT :limitNum", nativeQuery = true)
    List<CourseMapping> findCourseList(@Param("start") Point point, @Param("limitNum") Long limitNum);

    @Query(value = "SELECT c.title FROM Course c WHERE c.title = :title")
    Optional<String> findTitle(@Param("title") String title);
}