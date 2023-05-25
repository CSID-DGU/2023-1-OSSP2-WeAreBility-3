package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.security.jwt.JwtToken;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponseDto {
    private JwtToken jwt;
}