package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Advertisement;
import com.dongguk.cse.naemansan.domain.Image;
import com.dongguk.cse.naemansan.domain.Shop;
import com.dongguk.cse.naemansan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUser(User user);
    Optional<Image> findByShop(Shop shop);
    Optional<Image> findByAdvertisement(Advertisement advertisement);
    Optional<Image> findByUuidName(String uuidName);
}
