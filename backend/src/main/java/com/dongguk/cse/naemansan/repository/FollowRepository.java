package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    // User가 팔로우한 사람들을 가져오는 함수
    List<Follow> findByFollowingId(Long id);
    // User를 팔로우한 사람들을 가져오는 함수
    List<Follow> findByFollowedId(Long id);
}
