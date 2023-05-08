package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.CourseType;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseTypeRepository {
    public CourseType save(CourseType courseType);

}
