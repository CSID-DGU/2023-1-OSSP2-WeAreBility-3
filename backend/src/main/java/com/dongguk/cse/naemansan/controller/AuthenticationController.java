package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.domain.LoginRequest;
import com.dongguk.cse.naemansan.domain.LoginResponse;
import com.dongguk.cse.naemansan.dto.TokenDto;
import com.dongguk.cse.naemansan.dto.RedirectUrlDto;
import com.dongguk.cse.naemansan.security.jwt.JwtProvider;
import com.dongguk.cse.naemansan.service.GoogleService;
import com.dongguk.cse.naemansan.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final JwtProvider jwtProvider;
//    private final AppleService appleService;

    @GetMapping("/kakao")
    public RedirectUrlDto getKakaoRedirectUrl() {
        return kakaoService.getRedirectUrlDto("KAKAO");
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<LoginResponse> getKakaoAccessToken(@RequestParam("code") String code, HttpServletRequest request) {
        return ResponseEntity.ok(kakaoService.testAccessToken(code, request));
    }

    @PostMapping("/kakao")
    public ResponseEntity<LoginResponse> getKakaoAccessToken(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(kakaoService.login(request));
    }

    @GetMapping("/google")
    public RedirectUrlDto getGoogleRedirectUrl() {
        return googleService.getRedirectUrlDto("GOOGLE");
    }

    @GetMapping("/google/callback")
    public ResponseEntity<LoginResponse> getGoogleAccessToken(@RequestParam("code") String code, HttpServletRequest request) {
        return ResponseEntity.ok(googleService.testAccessToken(code, request));
    }

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> getGoogleAccessToken(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(googleService.login(request));
    }

//    @GetMapping("/apple")
//    public RedirectUrlDto getAppleRedirectUrl() {
//        return appleService.getRedirectUrlDto("APPLE");
//    }

//    @PostMapping("/kakao")
//    public ResponseEntity<LoginResponse> getAppleAccessToken(@RequestBody LoginRequest request) {
//        return ResponseEntity.ok((LoginResponse) appleService.login(request));
//    }


    // test용
    @PostMapping("/renewal")
    public ResponseEntity<TokenDto> UpdateAccessToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth =  headerAuth.substring(7, headerAuth.length());
        }

        return ResponseEntity.ok(TokenDto.builder().tokens(jwtProvider.validRefreshToken(headerAuth)).build());
    }
}