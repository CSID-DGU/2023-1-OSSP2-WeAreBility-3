package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Walkway;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Walkwayrepository {
    Walkway save(Walkway walkway);
    Optional<Walkway> findById(int id);
    List<Walkway> findAll();
    Optional<Walkway> orderByKeyword(String tag);
    Optional<Walkway> orderByLocation(String longitude, String latitude);



}
