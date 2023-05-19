package com.dongguk.cse.naemansan.security.filter;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.security.CustomUserDetail;
import com.dongguk.cse.naemansan.security.CustomUserDetailService;
import com.dongguk.cse.naemansan.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = JwtProvider.refineToken(request);
        Claims claims = jwtProvider.validateToken(token);

        String userid = claims.get("id").toString();

        CustomUserDetail userDetails = (CustomUserDetail) userDetailsService.loadUserByUsername(userid);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/auth");
    }
}
