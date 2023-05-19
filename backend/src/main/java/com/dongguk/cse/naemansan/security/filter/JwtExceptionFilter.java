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

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        boolean isException = false;
        try {
            filterChain.doFilter(request, response);
        } catch (SecurityException e) {
            request.setAttribute("exception", ErrorCode.ACCESS_DENIED_ERROR);
            isException = true;
        } catch (MalformedJwtException  e) {
            request.setAttribute("exception", ErrorCode.TOKEN_MALFORMED_ERROR);
            isException = true;
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_TYPE_ERROR);
            isException = true;
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_EXPIRED_ERROR);
            isException = true;
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_UNSUPPORTED_ERROR);
            isException = true;
        } catch (JwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_UNKNOWN_ERROR);
            isException = true;
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.NOT_FOUND_USER);
            isException = true;
        }

        if (isException) {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/auth");
    }
}
