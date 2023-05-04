package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Walkway;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;


public class JpaWalkwayRepository implements Walkwayrepository {
    private final EntityManager em;

    public JpaWalkwayRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Walkway save(Walkway walkway) {
        em.persist(walkway);
        return walkway;
    }

    @Override
    public Optional<Walkway> findById(int id) {
        Walkway walkway = em.find(Walkway.class, id);
        return Optional.ofNullable(walkway);
    }

    @Override
    public List<Walkway> findAll() {
        return em.createQuery("select c from courses c", Walkway.class)
                .getResultList();
    }

    @Override
    public Optional<Walkway> orderByKeyword(String tag) {
        List<Walkway> result = em.createQuery("select c from courses c, course_types t where t.tag=:tag AND t.id=c.id", Walkway.class)
                .setParameter("tag", tag)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<Walkway> orderByLocation(String longitude, String latitude) {
        List<Walkway> result = em.createQuery("select c , ST_Distance_Sphere(POINT(:longitude, :latitude),POINT(c.longitude, c.latitude)) AS distance from courses c where c.id IN (select c.id from courses c where ST_Distance_Sphere(POINT(:longitude, :latitude),POINT(c.longitude, c.latitude)) <= 2000) order by distance", Walkway.class)
                .setParameter("longitude", longitude).setParameter("latitude", latitude)
                .getResultList();
        return result.stream().findAny();
    }
}
