package com.dongguk.cse.naemansan.security.filter;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.security.jwt.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final String[] urls = { "/auth/kakao", "/auth/kakao/callback",
                                    "/auth/google", "/auth/google/callback",
                                    "/auth/apple", "/auth/apple/callback",
                                    "/auth/refresh", "/image"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        boolean isException = false;
        try {
            filterChain.doFilter(request, response);
        }
        catch (SecurityException e) {
            log.error("FilterException throw SecurityException Exception : {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.ACCESS_DENIED_ERROR);
            isException = true;
        }
        catch (MalformedJwtException  e) {
            log.error("FilterException throw MalformedJwtException Exception : {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.TOKEN_MALFORMED_ERROR);
            isException = true;
        } catch (IllegalArgumentException e) {
            log.error("FilterException throw IllegalArgumentException Exception : {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.TOKEN_TYPE_ERROR);
            isException = true;
        } catch (ExpiredJwtException e) {
            log.error("FilterException throw ExpiredJwtException Exception : {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.TOKEN_EXPIRED_ERROR);
            isException = true;
        } catch (UnsupportedJwtException e) {
            log.error("FilterException throw UnsupportedJwtException Exception : {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.TOKEN_UNSUPPORTED_ERROR);
            isException = true;
        } catch (JwtException e) {
            log.error("FilterException throw JwtException Exception : {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.TOKEN_UNKNOWN_ERROR);
            isException = true;
        } catch (Exception e) {
            log.error("FilterException throw Exception Exception : {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.NOT_FOUND_USER);
            isException = true;
        }

        if (isException) {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(urls).filter(url -> url.equals(request.getRequestURI())).count() > 0;
    }
}
