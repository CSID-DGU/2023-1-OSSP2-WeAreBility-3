package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
