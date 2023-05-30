package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Advertisement;
import com.dongguk.cse.naemansan.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    Optional<Advertisement> findByEnterpriseName(String name);

    Optional<Advertisement> findById(Long id);

    Optional<Advertisement> findByIdNotAndEnterpriseName(Long id, String name);
}
