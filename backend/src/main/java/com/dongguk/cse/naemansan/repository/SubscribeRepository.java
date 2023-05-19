package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    Optional<Subscribe> findByUserId(Long id);
}
