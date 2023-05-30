package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Badge;
import com.dongguk.cse.naemansan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    // 만약 Data가 없다면 Empty List가 나온다.
    List<Badge> findByUser(User user);

    // 연관관계가 없는 엔티티 조인 처리 (ON 사용)
    @Query("SELECT b.id, n.name, b.getDate FROM Badge b INNER JOIN b.badgeName n WHERE b.user.id = :userId ORDER BY b.getDate DESC")
    List<Object[]> findUserBadgeList(@Param("userId") Long userId);
}