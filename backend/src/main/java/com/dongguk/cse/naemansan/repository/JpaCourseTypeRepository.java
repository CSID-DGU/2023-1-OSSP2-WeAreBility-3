package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.CourseType;
import jakarta.persistence.EntityManager;

public class JpaCourseTypeRepository implements CourseTypeRepository {
    private final EntityManager em;

    public JpaCourseTypeRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public CourseType save(CourseType courseType) {
        em.persist(courseType);
        return courseType;
    }
}
