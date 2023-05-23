package com.dongguk.cse.naemansan.dto;

import com.dongguk.cse.naemansan.security.jwt.JwtToken;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private JwtToken jwt;
}