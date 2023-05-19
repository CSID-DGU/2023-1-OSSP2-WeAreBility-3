package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Follow;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.dto.FollowDto;
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
    public Boolean createFollow(Long userId, Long followingId) {
        Optional<Follow> follow = followRepository.findByFollowingIdAndFollowedId(userId, followingId);

        if (!follow.isEmpty()) {
            log.error("해당 유저는 이미 팔로잉 되어있습니다. userId - {} -> followingId - {}", userId, followingId);
            return Boolean.FALSE;
        }

        Follow save = followRepository.save(Follow.builder()
                .followingId(userId)
                .followedId(followingId).build());

        return Boolean.TRUE;
    }

    public List<FollowDto> readFollowing(Long userId) {
        List<Follow> follows = followRepository.findByFollowingId(userId);

        List<FollowDto> followDtos = new ArrayList<>();
        for (Follow follow : follows) {
            Optional<User> user = userRepository.findById(follow.getFollowedId());

            if (user.isEmpty()) {
                log.error("존재하지 않은 유저 입니다. userId - {}", userId);
                continue;
            }

            followDtos.add(FollowDto.builder()
                    .userId(user.get().getId())
                    .userName(user.get().getName()).build());
        }

        return followDtos;
    }

    public List<FollowDto> readFollower(Long userId) {
        List<Follow> follows = followRepository.findByFollowedId(userId);

        List<FollowDto> followDtos = new ArrayList<>();

        for (Follow follow : follows) {
            Optional<User> user = userRepository.findById(follow.getFollowingId());

            if (user.isEmpty()) {
                log.error("존재하지 않은 유저 입니다. userId - {}", userId);
                continue;
            }

            followDtos.add(FollowDto.builder()
                    .userId(user.get().getId())
                    .userName(user.get().getName()).build());
        }

        return followDtos;
    }

    public Boolean deleteFollow(Long userId, Long followingId) {
        Optional<Follow> follow = followRepository.findByFollowingIdAndFollowedId(userId, followingId);

        if (follow.isEmpty()) {
            log.error("해당 유저는 이미 팔로잉 중이지 않습니다. userId - {} -> followingId - {}", userId, followingId);
            return Boolean.FALSE;
        }

        followRepository.deleteByFollowingIdAndFollowedId(userId, followingId);

        return Boolean.TRUE;
    }
}
