package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.domain.LoginRequest;
import com.dongguk.cse.naemansan.domain.LoginResponse;
import com.dongguk.cse.naemansan.dto.RedirectUrlDto;
import com.dongguk.cse.naemansan.service.AppleService;
import com.dongguk.cse.naemansan.service.GoogleService;
import com.dongguk.cse.naemansan.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auths")
@RequiredArgsConstructor
public class AuthController {
    private final KakaoService kakaoService;
    private final GoogleService googleService;
//    private final AppleService appleService;

    @GetMapping("/kakao")
    public RedirectUrlDto getKakaoRedirectUrl() {
        return kakaoService.getRedirectUrlDto("KAKAO");
    }

    @PostMapping("/kakao")
    public ResponseEntity<LoginResponse> getKakaoAccessToken(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(kakaoService.login(request));
    }

    @GetMapping("/google")
    public RedirectUrlDto getGoogleRedirectUrl() {
        return googleService.getRedirectUrlDto("GOOGLE");
    }

//    @PostMapping("/google")
//    public ResponseEntity<LoginResponse> getGoogleAccessToken(@RequestBody LoginRequest request) {
//        return ResponseEntity.ok((LoginResponse) googleService.login(request));
//    }

//    @GetMapping("/apple")
//    public RedirectUrlDto getAppleRedirectUrl() {
//        return appleService.getRedirectUrlDto("APPLE");
//    }

//    @PostMapping("/kakao")
//    public ResponseEntity<LoginResponse> getAppleAccessToken(@RequestBody LoginRequest request) {
//        return ResponseEntity.ok((LoginResponse) appleService.login(request));
//    }

}