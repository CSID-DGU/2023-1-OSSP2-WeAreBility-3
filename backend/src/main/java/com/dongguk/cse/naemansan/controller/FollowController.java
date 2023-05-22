package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    // UserOne이 UserTwo를 팔로우함 - Follow Create
    @PostMapping("/{followingId}")
    public ResponseDto<Boolean> createFollow(Authentication authentication, @PathVariable Long followingId) {
        return new ResponseDto<Boolean>(followService.createFollow(Long.valueOf(authentication.getName()), followingId));
    }

    // UserOne이 UserTwo를 팔로우를 취소함 - Follow Delete
    @DeleteMapping("/{followingId}")
    public ResponseDto<Boolean> deleteFollow(Authentication authentication, @PathVariable Long followingId) {
        return new ResponseDto<Boolean>(followService.deleteFollow(Long.valueOf(authentication.getName()), followingId));
    }
}
