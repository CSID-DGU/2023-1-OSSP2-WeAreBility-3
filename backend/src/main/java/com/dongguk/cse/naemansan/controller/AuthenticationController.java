package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.response.LoginResponse;
import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import com.dongguk.cse.naemansan.dto.response.TokenDto;
import com.dongguk.cse.naemansan.security.jwt.JwtProvider;
import com.dongguk.cse.naemansan.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtProvider jwtProvider;

    @GetMapping("/kakao")
    public ResponseEntity<Map<String, String>> getKakaoRedirectUrl() {
        Map<String, String> map = new HashMap<>();
        map.put("Url", authenticationService.getRedirectUrl(LoginProviderType.KAKAO));
        return ResponseEntity.ok(map);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<LoginResponse> loginKakao(@RequestParam("code") String code) {
        return ResponseEntity.ok(authenticationService.login(code, LoginProviderType.KAKAO));
    }

    @GetMapping("/google")
    public ResponseEntity<Map<String, String>> getGoogleRedirectUrl() {
        Map<String, String> map = new HashMap<>();
        map.put("Url", authenticationService.getRedirectUrl(LoginProviderType.GOOGLE));
        return ResponseEntity.ok(map);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<LoginResponse> loginGoogle(@RequestParam("code") String code) {
        return ResponseEntity.ok(authenticationService.login(code, LoginProviderType.GOOGLE));
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
    public ResponseEntity<TokenDto> UpdateAccessToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth =  headerAuth.substring(7, headerAuth.length());
        }

        return ResponseEntity.ok(TokenDto.builder()
                .token(jwtProvider.validRefreshToken(headerAuth)).build());
    }
}