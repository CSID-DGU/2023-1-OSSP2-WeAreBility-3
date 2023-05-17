package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Follow;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.dto.response.FollowDto;
import com.dongguk.cse.naemansan.repository.FollowRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    public Boolean createFollow(Long followingId, Long followerId) {
        Optional<User> followingUser = userRepository.findById(followingId);
        Optional<User> followerUser = userRepository.findById(followerId);
        Optional<Follow> follow = followRepository.findByFollowingUserAndFollowerUser(followingUser.get(), followerUser.get());

        if (!follow.isEmpty()) {
            log.error("해당 유저는 이미 팔로잉 되어있습니다. userId - {} -> followingId - {}", followingId, followerId);
            return Boolean.FALSE;
        }

        Follow save = followRepository.save(Follow.builder()
                .followingUser(followingUser.get())
                .followerUser(followerUser.get()).build());

        return Boolean.TRUE;
    }

    public List<FollowDto> readFollowing(Long followingId) {
        Optional<User> followingUser = userRepository.findById(followingId);
        List<Follow> follows = followRepository.findByFollowingUser(followingUser.get());

        List<FollowDto> followDtos = new ArrayList<>();
        for (Follow follow : follows) {
            followDtos.add(FollowDto.builder()
                    .userId(follow.getFollowerUser().getId())
                    .userName(follow.getFollowerUser().getName()).build());
        }

        return followDtos;
    }

    public List<FollowDto> readFollower(Long followerId) {
        Optional<User> followerUser = userRepository.findById(followerId);
        List<Follow> follows = followRepository.findByFollowerUser(followerUser.get());

        List<FollowDto> followDtos = new ArrayList<>();

        for (Follow follow : follows) {
            followDtos.add(FollowDto.builder()
                    .userId(follow.getFollowingUser().getId())
                    .userName(follow.getFollowingUser().getName()).build());
        }

        return followDtos;
    }

    public Boolean deleteFollow(Long followingId, Long followerId) {
        Optional<User> followingUser = userRepository.findById(followingId);
        Optional<User> followerUser = userRepository.findById(followerId);
        Optional<Follow> follow = followRepository.findByFollowingUserAndFollowerUser(followingUser.get(), followerUser.get());

        if (follow.isEmpty()) {
            log.error("해당 유저는 이미 팔로잉 중이지 않습니다. followingId: {} -> followerId: {}", followingId, followerId);
            return Boolean.FALSE;
        }

        followRepository.deleteByFollowingUserAndFollowerUser(followingUser.get(), followerUser.get());

        return Boolean.TRUE;
    }
}
