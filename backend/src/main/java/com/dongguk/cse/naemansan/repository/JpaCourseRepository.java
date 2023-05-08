package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.domain.CourseType;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import jakarta.persistence.EntityManager;
import org.springframework.data.geo.Point;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public class JpaCourseRepository implements CourseRepository {
    private final EntityManager em;

    public JpaCourseRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Course save(Course course) {
        em.persist(course);
        return course;
    }
    @Override
    public Optional<Course> findById(Long id) {
        Course course = em.find(Course.class, id);
        return Optional.ofNullable(course);
    }

    @Override
    public Optional<Course> findByTitle(String title) {
        List<Course> result = em.createQuery("select c from Course c where c.title=:title", Course.class)
                .setParameter("title", title)
                .getResultList();
        return result.stream().findAny();
    }
    @Override
    public List<Course> findAll() {
        return em.createQuery("select c from Course c", Course.class)
                .getResultList();
    }

    @Override
    public Optional<Course> orderByKeyword(String tag) {
        List<Course> result = em.createQuery("select c from Course c, CourseType t where t.courseTagType=:tag AND t.courseId=c.id", Course.class)
                .setParameter("tag", tag)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<Course> orderByLocation(Point point) {
        double longitude, latitude;
        longitude = point.getX();
        latitude = point.getY();
        List<Course> result = em.createQuery("select c , ST_Distance_Sphere(POINT(:longitude, :latitude),c.startpoint) from Course c where c.id IN (select c.id from Course c where ST_Distance_Sphere(POINT(:longitude, :latitude),c.startpoint) <= 2000) order by distance", Course.class)
                .setParameter("longitude", longitude).setParameter("latitude", latitude)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = em.find(Course.class, id);
        em.remove(course);
        //tx
    }

    @Override
    public Long updateCourse(Long id, String title, Date created_date, String introduction, String start_location, int status) {
        Course course = em.find(Course.class, id);
        course.setTitle(title);
        course.setCreated_date(created_date);
        course.setIntroduction(introduction);
        course.setStart_location(start_location);
        course.setStatus(status);
        return course.getId();
    }
}
