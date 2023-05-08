package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.CourseType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseTypeRepository {
    CourseType save(CourseType courseType);
    Optional<CourseType> findById(Long id);
    Optional<CourseType> findByCourseId(int id);
    void deleteCourseTag(int id);

    //void updateCourseTag();



}
