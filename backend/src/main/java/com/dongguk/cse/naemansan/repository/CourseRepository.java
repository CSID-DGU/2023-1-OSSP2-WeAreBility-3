package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.domain.CourseType;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository {
    Course save(Course course);
    Optional<Course> findById(Long id);
    Optional<Course> findByTitle(String title);
    List<Course> findAll();
    Optional<Course> orderByKeyword(String tag);
    Optional<Course> orderByLocation(Point point);

    void deleteCourse(Long id);

    Long updateCourse(Long id, String title, Date created_date, String introduction, String start_location, int status);
}
