package com.dongguk.cse.naemansan.util;

import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class JwtUtil {
    public static String refineToken(String beforeToken) {
        String afterToken = null;

        try {
            if (StringUtils.hasText(beforeToken) && beforeToken.startsWith("Bearer ")) {
                afterToken =  beforeToken.substring(7, beforeToken.length());
            } else {
                throw new UnsupportedJwtException("잘못된 형식의 Token 입니다.");
            }
        } catch (UnsupportedJwtException e) {
            log.info("{}", e);
        }

        return afterToken;
    }
}
