package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
