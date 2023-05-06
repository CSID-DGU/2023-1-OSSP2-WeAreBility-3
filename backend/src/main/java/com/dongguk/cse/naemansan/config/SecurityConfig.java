package com.dongguk.cse.naemansan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


import lombok.RequiredArgsConstructor;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .httpBasic().disable()          // ui가 아닌 token 인증형식
                .csrf().disable()               // cross 쪽
                .cors().and()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()                    // 누구나 로그인 및 토큰 발행 가능
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .and()
//                .addFilterBefore(new JsonWebTokenFilter(userService, secretkey), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}