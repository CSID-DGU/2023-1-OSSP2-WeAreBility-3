package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.ResponseDto;
import com.dongguk.cse.naemansan.dto.UserDto;
import com.dongguk.cse.naemansan.dto.UserRequestDto;
import com.dongguk.cse.naemansan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/user")
    public ResponseDto<UserDto> readUserProfile(Authentication authentication) {
        return new ResponseDto<UserDto>(userService.getUserInformation(Long.valueOf(authentication.getName())));
    }

    @PutMapping("/user")
    public ResponseDto<Boolean> updateUserProfile(@RequestBody UserRequestDto userRequestDto, Authentication authentication) {
        return new ResponseDto<Boolean>(userService.updateUserInformation(Long.valueOf(authentication.getName()), userRequestDto));
    }

    @DeleteMapping("/user")
    public ResponseDto<Boolean> deleteUserProfile(Authentication authentication) {
        return new ResponseDto<Boolean>(userService.deleteUserInformation(Long.valueOf(authentication.getName())));
    }
}
