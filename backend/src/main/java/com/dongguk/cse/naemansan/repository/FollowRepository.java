package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Follow;
import com.sun.source.tree.ForLoopTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    // User가 팔로우한 사람들을 가져오는 함수
    List<Follow> findByFollowingId(Long userId);
    // User를 팔로우한 사람들을 가져오는 함수
    List<Follow> findByFollowedId(Long userId);

    Optional<Follow> findByFollowingIdAndFollowedId(Long followingId, Long followerId);

    void deleteByFollowingIdAndFollowedId(Long followingId, Long followerId);
}
