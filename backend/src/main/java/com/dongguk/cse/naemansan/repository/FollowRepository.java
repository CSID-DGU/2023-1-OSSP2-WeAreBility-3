package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Follow;
import com.dongguk.cse.naemansan.domain.User;
import com.sun.source.tree.ForLoopTree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    // User 가 팔로우한 사람들을 가져오는 Method
    Page<Follow> findByFollowingUser(User user, Pageable pageable);
    // User 를 팔로우한 사람들을 가져오는 Method
    Page<Follow> findByFollowerUser(User user, Pageable pageable);
    // User One 이 User Two 를 Follow 했는지 확인하는 Method
    Optional<Follow> findByFollowingUserAndFollowerUser(User followingUser, User followerUser);
    //User One 이 User Two 를 Follow 를 삭제하는 Method
    void deleteByFollowingUserAndFollowerUser(User followingUser, User followerUser);
}
