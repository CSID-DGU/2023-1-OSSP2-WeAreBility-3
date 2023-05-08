package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    // 만약 Data가 없다면 Empty List가 나온다.
    List<Badge> findByUserId(Long id);
}
