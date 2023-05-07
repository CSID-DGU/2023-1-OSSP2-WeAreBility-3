package com.dongguk.cse.naemansan.config;

import com.dongguk.cse.naemansan.repository.UserRepository;
import com.dongguk.cse.naemansan.security.CustomUserDetailService;
import com.dongguk.cse.naemansan.security.JwtEntryPoint;
import com.dongguk.cse.naemansan.security.filter.JwtAuthenticationFilter;
import com.dongguk.cse.naemansan.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider tokenService;
    private final CustomUserDetailService customUserDetailService;
    private final JwtEntryPoint jwtPoint;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeHttpRequests()
                    .anyRequest().permitAll()               // 개발용이므로 지워야함!!!!!!!
//                    .requestMatchers("/auth/**").permitAll()
//                    .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtPoint)
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(tokenService, customUserDetailService), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}