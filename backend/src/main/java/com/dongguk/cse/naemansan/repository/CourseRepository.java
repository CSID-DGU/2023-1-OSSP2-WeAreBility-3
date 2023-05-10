package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Course;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByTitle(String title);

    @Query(value = "SELECT c FROM Course c WHERE ST_Distance_Sphere(:start, c.startLocation) <= 1000")
    Page<Course> findCourseList(@Param("start") Point point, Pageable paging);
}
