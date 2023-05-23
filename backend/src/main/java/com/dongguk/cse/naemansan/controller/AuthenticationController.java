package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.dto.response.JwtResponseDto;
import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import com.dongguk.cse.naemansan.dto.response.TokenDto;
import com.dongguk.cse.naemansan.security.jwt.JwtProvider;
import com.dongguk.cse.naemansan.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtProvider jwtProvider;

    @GetMapping("/kakao")
    public ResponseDto<Map<String, String>> getKakaoRedirectUrl() {
        Map<String, String> map = new HashMap<>();
        map.put("url", authenticationService.getRedirectUrl(LoginProviderType.KAKAO));
        return new ResponseDto(map);
    }

    @GetMapping("/kakao/callback")
    public ResponseDto<JwtResponseDto> loginKakao(@RequestParam("code") String code) {
        return new ResponseDto(authenticationService.login(code, LoginProviderType.KAKAO));
    }

    @GetMapping("/google")
    public ResponseDto<Map<String, String>> getGoogleRedirectUrl() {
        Map<String, String> map = new HashMap<>();
        map.put("url", authenticationService.getRedirectUrl(LoginProviderType.GOOGLE));
        return new ResponseDto(map);
    }

    @GetMapping("/google/callback")
    public ResponseDto<JwtResponseDto> loginGoogle(@RequestParam("code") String code) {
        return new ResponseDto(authenticationService.login(code, LoginProviderType.GOOGLE));
    }

//    @GetMapping("/apple")
//    public RedirectUrlDto getAppleRedirectUrl() {
//        return appleService.getRedirectUrlDto("APPLE");
//    }

//    @PostMapping("/kakao")
//    public ResponseEntity<LoginResponse> getAppleAccessToken(@RequestBody LoginRequest request) {
//        return ResponseEntity.ok((LoginResponse) appleService.login(request));
//    }

    @GetMapping("/logout")
    public void logoutCommon(Authentication authentication) {
        authenticationService.logout(Long.valueOf(authentication.getName()));
    }

    @GetMapping("/withdrawal")
    public void withdrawalCommon(Authentication authentication) {
        authenticationService.withdrawal(Long.valueOf(authentication.getName()));
    }

    // test용
    @PostMapping("/refresh")
    public ResponseDto<TokenDto> UpdateAccessToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth =  headerAuth.substring(7, headerAuth.length());
        }

        return new ResponseDto(TokenDto.builder()
                .token(jwtProvider.validRefreshToken(headerAuth)).build());
    }
}