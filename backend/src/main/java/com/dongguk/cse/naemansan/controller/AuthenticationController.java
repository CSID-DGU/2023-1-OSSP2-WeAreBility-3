package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.dto.response.JwtResponseDto;
import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import com.dongguk.cse.naemansan.dto.response.TokenDto;
import com.dongguk.cse.naemansan.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("/kakao")
    public ResponseDto<Map<String, String>> getKAKAO_REDIRECT_URL() {
        Map<String, String> map = new HashMap<>();
        map.put("url", authenticationService.getRedirectUrl(LoginProviderType.KAKAO));
        return new ResponseDto(map);
    }

    @GetMapping("/kakao/callback")
    public ResponseDto<JwtResponseDto> loginKakao(@RequestParam("code") String code) {
        return new ResponseDto(authenticationService.login(code, LoginProviderType.KAKAO));
    }

    @GetMapping("/google")
    public ResponseDto<Map<String, String>> getGOOGLE_REDIRECT_URL() {
        Map<String, String> map = new HashMap<>();
        map.put("url", authenticationService.getRedirectUrl(LoginProviderType.GOOGLE));
        return new ResponseDto(map);
    }

    @GetMapping("/google/callback")
    public ResponseDto<JwtResponseDto> loginGoogle(@RequestParam("code") String code) {
        return new ResponseDto<JwtResponseDto>(authenticationService.login(code, LoginProviderType.GOOGLE));
    }

    @GetMapping("/apple")
    public ResponseDto<Map<String, String>> getAppleRedirectUrl() {
        Map<String, String> map = new HashMap<>();
        map.put("url", authenticationService.getRedirectUrl(LoginProviderType.APPLE));
        return new ResponseDto(map);
    }

    @PostMapping(value = "/apple/callback")
    public ResponseDto<?> loginApple(@RequestParam("code") String code) {
        return new ResponseDto<JwtResponseDto>(authenticationService.login(code, LoginProviderType.APPLE));
    }

    @GetMapping("/logout")
    public ResponseDto<Boolean> logoutCommon(Authentication authentication) {
        return new ResponseDto<Boolean>(authenticationService.logout(Long.valueOf(authentication.getName())));
    }

    @PostMapping("/refresh")
    public ResponseDto<TokenDto> UpdateAccessToken(HttpServletRequest request) {
        return new ResponseDto(authenticationService.getAccessTokenByRefreshToken(request));
    }
}