package com.dongguk.cse.naemansan.security.jwt;

import com.dongguk.cse.naemansan.domain.RefreshToken;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.domain.type.UserRoleType;
import com.dongguk.cse.naemansan.repository.UserRepository;
import io.jsonwebtoken.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider implements InitializingBean {

    private final UserRepository userRepository;
    @Value("${jwt.secret: abc}")
    private String secretKey;
    private Key key;
    private static final Long accessExpiredMs = 60 * 60 * 2 * 1000l;
    private static final Long refrechExpiredMs = 60 * 60 * 24 * 60 * 1000l;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * token 생성
     */
    public String createToken(Long id, UserRoleType userRoleType, boolean isAccess) {
        Claims claims = Jwts.claims();

        claims.put("id", id);
        if(isAccess) {
            claims.put("userRoleType", userRoleType);
        }

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (isAccess ? accessExpiredMs : refrechExpiredMs)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * accessToken, refreshToken 생성
     */
    public JwtToken createTotalToken(Long id, UserRoleType userRoleType) {

        //Access token 생성
        String accessToken = createToken(id, userRoleType, true);

        //Refresh token 생성
        String refreshToken = createToken(id, userRoleType, false);

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * refreshToken validation 체크(refresh token 이 넘어왔을때)
     * 정상 - access 토큰 생성후 반환
     * 비정상 - null
     */
    public String validRefreshToken(String token) {

        String refreshToken = token;

        if(!validateToken(refreshToken)) {
            throw new NullPointerException();
        }

        // test용
//        refreshToken = refreshToken.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        Optional<User> user = userRepository.findById(Long.valueOf(claims.get("id").toString()));

        if (user.isEmpty()) {
            throw new NullPointerException();
        }

        return createToken(user.get().getId(), user.get().getUserRoleType(), true);
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            // test용
//            String token = jwtToken.substring(7);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
