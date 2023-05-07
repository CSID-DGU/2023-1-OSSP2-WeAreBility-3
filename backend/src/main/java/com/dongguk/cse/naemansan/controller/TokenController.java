package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.domain.type.UserRoleType;
import com.dongguk.cse.naemansan.dto.TokenDto;
import com.dongguk.cse.naemansan.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final JwtProvider jwtProvider;

    @GetMapping("/token/expired")
    public String auth() {
        throw new RuntimeException();
    }

    @GetMapping("/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Refresh");

        if (token != null && jwtProvider.validateToken(token)) {
            String email = "1234@gmail.com";
            String newToken = jwtProvider.createToken(1234L, UserRoleType.USER,true);

            response.addHeader("Auth", newToken);
            response.setContentType("application/json;charset=UTF-8");

            return "HAPPY NEW TOKEN";
        }

        throw new RuntimeException();
    }

    @GetMapping("/token/success")
    public ResponseEntity<TokenDto> getToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(null);
    }
}
