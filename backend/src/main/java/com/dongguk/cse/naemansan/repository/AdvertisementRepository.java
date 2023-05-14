package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}
