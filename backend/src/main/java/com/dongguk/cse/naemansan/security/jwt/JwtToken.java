package com.dongguk.cse.naemansan.security.jwt;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
@Data
@Getter
@Builder
public class JwtToken {
    private String access_token;
    private String refresh_token;
}
