package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import com.dongguk.cse.naemansan.security.jwt.JwtToken;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private JwtToken jwt;
}