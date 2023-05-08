package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.CourseType;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

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
    @Override
    public Optional<CourseType> findById(Long id){
        CourseType courseType = em.find(CourseType.class,id);
        return Optional.ofNullable(courseType);
    }
    @Override
    public Optional<CourseType> findByCourseId(int id){
        List<CourseType> result = em.createQuery("select t from CourseType t where t.courseId=:id",CourseType.class)
                .setParameter("id",id)
                .getResultList();
        return result.stream().findAny();
    }
    @Override
    public void deleteCourseTag(int id){
        em.createQuery("delete");
    }

}
