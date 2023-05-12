package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.domain.LoginResponse;
import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import com.dongguk.cse.naemansan.dto.TokenDto;
import com.dongguk.cse.naemansan.security.jwt.JwtProvider;
import com.dongguk.cse.naemansan.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtProvider jwtProvider;
//    private final AppleService appleService;

    @GetMapping("/kakao")
    public ResponseEntity<String> getKakaoRedirectUrl() {
        return ResponseEntity.ok(authenticationService.getRedirectUrl(LoginProviderType.KAKAO));
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<LoginResponse> getKakaoAccessToken(@RequestParam("code") String code, HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.login(code, LoginProviderType.KAKAO, request));
    }

    @GetMapping("/google")
    public ResponseEntity<String> getGoogleRedirectUrl() {
        return ResponseEntity.ok(authenticationService.getRedirectUrl(LoginProviderType.GOOGLE));
    }

    @GetMapping("/google/callback")
    public ResponseEntity<LoginResponse> getGoogleAccessToken(@RequestParam("code") String code, HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.login(code, LoginProviderType.GOOGLE, request));
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