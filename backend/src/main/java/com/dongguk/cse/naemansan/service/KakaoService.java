package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.LoginRequest;
import com.dongguk.cse.naemansan.dto.RedirectUrlDto;
import org.springframework.stereotype.Service;

@Service
public class KakaoService {
    private final String authorizeURL = "https://kauth.kakao.com/oauth/authorize";
    private final String clientID = "db9bfe01c95414e0f1add469fdfaa9ff";
    private final String redirectURL = "http://localhost:8080/login/oauth2/code/kakao";

    public RedirectUrlDto getRedirectUrlDto(String ProviedType) {
        String url = authorizeURL + "?client_id=" + clientID + "&redirect_uri="
                + redirectURL + "&response_type=code";
        return new RedirectUrlDto(url, ProviedType);
    }
    public Object login(LoginRequest request) {
        return null;
    }
}
