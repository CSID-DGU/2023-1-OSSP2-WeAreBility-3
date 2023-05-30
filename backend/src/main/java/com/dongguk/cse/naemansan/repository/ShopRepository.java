package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Shop;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByShopName(String name);
    Optional<Shop> findById(Long id);
    Optional<Shop> findByIdNotAndShopName(Long id, String name);

    @Query(value = "SELECT s.id AS id, ST_Distance_Sphere(:start, s.shop_location) AS radius FROM shops s "
            + "WHERE ST_Distance_Sphere(:start, s.shop_location) <= 10000",
            countQuery = "SELECT s.id AS id, ST_Distance_Sphere(:start, s.shop_location) AS radius FROM shops s "
                    + "WHERE ST_Distance_Sphere(:start, s.shop_location) <= 10000",
            nativeQuery = true)
    Page<ShopLocationForm> findListByLocation(@Param("start") Point point, Pageable pageable);

    public interface ShopLocationForm {
        Long getId();
        Double getRadius();
    }
}
