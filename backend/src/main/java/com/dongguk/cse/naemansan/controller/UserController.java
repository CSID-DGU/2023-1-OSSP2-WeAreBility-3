package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.request.UserDeviceRequestDto;
import com.dongguk.cse.naemansan.dto.request.UserPaymentRequestDto;
import com.dongguk.cse.naemansan.dto.request.UserTagRequestDto;
import com.dongguk.cse.naemansan.dto.response.*;
import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.dto.request.UserRequestDto;
import com.dongguk.cse.naemansan.service.BadgeService;
import com.dongguk.cse.naemansan.service.FollowService;
import com.dongguk.cse.naemansan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BadgeService badgeService;
    private final FollowService followService;
    @GetMapping("")
    public ResponseDto<UserDto> readUser(Authentication authentication) {
        return new ResponseDto<UserDto>(userService.readUserProfile(Long.valueOf(authentication.getName())));
    }
    @GetMapping("/{otherUserId}")
    public ResponseDto<UserDto> readUser(@PathVariable Long otherUserId) {
        return new ResponseDto<UserDto>(userService.readUserProfile(otherUserId));
    }

    @PutMapping("")
    public ResponseDto<UserDto> updateUser(Authentication authentication, @RequestBody UserRequestDto userRequestDto) {
        return new ResponseDto<UserDto>(userService.updateUserProfile(Long.valueOf(authentication.getName()), userRequestDto));
    }

    @DeleteMapping("")
    public ResponseDto<Boolean> deleteUser(Authentication authentication) {
        return new ResponseDto<Boolean>(userService.deleteUserProfile(Long.valueOf(authentication.getName())));
    }

    @GetMapping("/badge")
    public ResponseDto<List<BadgeDto>> readBadgeList(Authentication authentication) {
        return new ResponseDto<List<BadgeDto>>(badgeService.readBadgeList(Long.valueOf(authentication.getName())));
    }

    @GetMapping("/comment")
    public ResponseDto<List<CommentListDto>> readCommentList(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<CommentListDto>>(userService.readCommentList(Long.valueOf(authentication.getName()), page, num));
    }

    // User가 팔로우한 사람들의 List를 얻음 - Follow Read#1
    @GetMapping("/following")
    public ResponseDto<List<FollowDto>> readFollowing(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<FollowDto>>(followService.readFollowing(Long.valueOf(authentication.getName()), page, num));
    }

    // User를 팔로우한 사람들의 List를 얻음 - Follow Read#2
    @GetMapping("/follower")
    public ResponseDto<List<FollowDto>> readFollower(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<FollowDto>>(followService.readFollower(Long.valueOf(authentication.getName()), page, num));
    }

    @PostMapping("/tags")
    public ResponseDto<?> createUserTag(Authentication authentication, @RequestBody UserTagRequestDto requestDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("tags", userService.createTagByUserChoice(Long.valueOf(authentication.getName()), requestDto));
        return new ResponseDto<Map<String, Object>>(map);
    }

    @GetMapping("/tags")
    public ResponseDto<?> readUserTag(Authentication authentication) {
        Map<String, Object> map = new HashMap<>();
        map.put("tags", userService.readTagByUserChoice(Long.valueOf(authentication.getName())));
        return new ResponseDto<Map<String, Object>>(map);
    }

    @PutMapping("/tags")
    public ResponseDto<?> readUserTag(Authentication authentication, @RequestBody UserTagRequestDto requestDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("tags", userService.updateTagByUserChoice(Long.valueOf(authentication.getName()), requestDto));
        return new ResponseDto<Map<String, Object>>(map);
    }

    @PutMapping("/notification")
    public ResponseDto<?> updateUserDevice(Authentication authentication, @RequestBody UserDeviceRequestDto requestDto) {
        return new ResponseDto<Boolean>(userService.updateUserDevice(Long.valueOf(authentication.getName()), requestDto));
    }

    @PutMapping("/payment")
    public  ResponseDto updatePremium(Authentication authentication, @RequestBody UserPaymentRequestDto requestDto) {
        return new ResponseDto<Boolean>(userService.updatePremium(Long.valueOf(authentication.getName()), requestDto));
    }
}
