package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.CommentDto;
import com.dongguk.cse.naemansan.dto.FollowDto;
import com.dongguk.cse.naemansan.dto.ResponseDto;
import com.dongguk.cse.naemansan.dto.UserDto;
import com.dongguk.cse.naemansan.dto.request.UserRequestDto;
import com.dongguk.cse.naemansan.dto.response.BadgeDto;
import com.dongguk.cse.naemansan.dto.response.CourseDto;
import com.dongguk.cse.naemansan.service.BadgeService;
import com.dongguk.cse.naemansan.service.FollowService;
import com.dongguk.cse.naemansan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return new ResponseDto<Boolean>(userService.deleteUserInformation(Long.valueOf(authentication.getName())));
    }

    @GetMapping("/badge")
    public ResponseDto<List<BadgeDto>> readBadgeList(Authentication authentication) {
        return new ResponseDto<List<BadgeDto>>(badgeService.readBadgeList(Long.valueOf(authentication.getName())));
    }

    @GetMapping("/comment")
    public ResponseDto<List<CommentDto>> readCommentList(Authentication authentication) {
        return new ResponseDto<List<CommentDto>>(userService.readCommentList(Long.valueOf(authentication.getName())));
    }

    @GetMapping("/likeCourse")
    public ResponseDto<List<CourseDto>> readLikeCourseList(Authentication authentication) {
        return new ResponseDto<List<CourseDto>>(userService.readLikeCourseList(Long.valueOf(authentication.getName())));
    }

    @GetMapping("/enrollmentCourse")
    public ResponseDto<List<CourseDto>> readEnrollmentCourseList(Authentication authentication) {
        return new ResponseDto<List<CourseDto>>(userService.readEnrollmentCourseList(Long.valueOf(authentication.getName())));
    }

    @GetMapping("/finishCourse")
    public ResponseDto<List<CourseDto>> readFinishCourseList(Authentication authentication) {
        return new ResponseDto<List<CourseDto>>(userService.readFinishCourseList(Long.valueOf(authentication.getName())));
    }

    // User가 팔로우한 사람들의 List를 얻음 - Follow Read#1
    @GetMapping("/following")
    public ResponseDto<List<FollowDto>> readFollowing(Authentication authentication) {
        return new ResponseDto<List<FollowDto>>(followService.readFollowing(Long.valueOf(authentication.getName())));
    }

    // User를 팔로우한 사람들의 List를 얻음 - Follow Read#2
    @GetMapping("/follower")
    public ResponseDto<List<FollowDto>> readFollower(Authentication authentication) {
        return new ResponseDto<List<FollowDto>>(followService.readFollower(Long.valueOf(authentication.getName())));
    }
}
