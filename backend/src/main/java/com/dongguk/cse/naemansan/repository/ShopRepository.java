package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByShopName(String name);
    Optional<Shop> findById(Long id);
    Optional<Shop> findByIdNotAndShopName(Long id, String name);
}
