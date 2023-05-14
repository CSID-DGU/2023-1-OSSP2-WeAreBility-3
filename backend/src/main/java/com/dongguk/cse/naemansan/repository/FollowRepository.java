package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Follow;
import com.dongguk.cse.naemansan.domain.User;
import com.sun.source.tree.ForLoopTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    // User가 팔로우한 사람들을 가져오는 함수
    List<Follow> findByFollowingUser(User user);
    // User를 팔로우한 사람들을 가져오는 함수
    List<Follow> findByFollowerUser(User user);

    Optional<Follow> findByFollowingUserAndFollowerUser(User followingUser, User followerUser);

    void deleteByFollowingUserAndFollowerUser(User followingUser, User followerUser);
}
