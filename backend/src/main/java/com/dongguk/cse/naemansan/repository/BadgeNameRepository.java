package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.BadgeName;
import com.dongguk.cse.naemansan.domain.type.BadgeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeNameRepository extends JpaRepository<BadgeName, Long> {
    List<BadgeName> findByTypeOrderByConditionNumDesc(BadgeType type);
}