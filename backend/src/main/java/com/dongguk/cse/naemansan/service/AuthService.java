package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.LoginRequest;
import com.dongguk.cse.naemansan.domain.LoginResponse;
import com.dongguk.cse.naemansan.dto.RedirectUrlDto;

import java.util.Map;

public interface AuthService {
    public RedirectUrlDto getRedirectUrlDto(String proviedType);
    public LoginResponse login(LoginRequest request);
    public Map<String, Object> getAccessToken(String code);
    public Map<String, Object> getUserInfo(String code);
    public void logout(String accessToken);
}
