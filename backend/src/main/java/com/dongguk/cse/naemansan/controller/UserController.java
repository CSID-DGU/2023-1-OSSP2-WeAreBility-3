package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.ResponseDto;
import com.dongguk.cse.naemansan.dto.UserDto;
import com.dongguk.cse.naemansan.dto.request.UserRequestDto;
import com.dongguk.cse.naemansan.dto.response.BadgeDto;
import com.dongguk.cse.naemansan.service.BadgeService;
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
    @GetMapping("")
    public ResponseDto<UserDto> readUser(Authentication authentication) {
        return new ResponseDto<UserDto>(userService.getUserInformation(Long.valueOf(authentication.getName())));
    }
    @GetMapping("/{otherUserId}")
    public ResponseDto<UserDto> readUser(@PathVariable Long otherUserId) {
        return new ResponseDto<UserDto>(userService.getUserInformation(otherUserId));
    }

    @PutMapping("")
    public ResponseDto<Boolean> updateUser(Authentication authentication, @RequestBody UserRequestDto userRequestDto) {
        return new ResponseDto<Boolean>(userService.updateUserInformation(Long.valueOf(authentication.getName()), userRequestDto));
    }

    @DeleteMapping("")
    public ResponseDto<Boolean> deleteUser(Authentication authentication) {
        return new ResponseDto<Boolean>(userService.deleteUserInformation(Long.valueOf(authentication.getName())));
    }

    @GetMapping("/badge")
    public ResponseDto<List<BadgeDto>> readBadgeList(Authentication authentication) {
        return new ResponseDto<List<BadgeDto>>(badgeService.readBadgeList(Long.valueOf(authentication.getName())));
    }
}
