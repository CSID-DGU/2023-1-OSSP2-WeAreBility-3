package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.LoginRequest;
import com.dongguk.cse.naemansan.dto.RedirectUrlDto;
import org.springframework.stereotype.Service;

@Service
public class GoogleService {
    private final String authorizeURL = "https://accounts.google.com/o/oauth2/auth";
    private final String clientID = "99694127428-0j6odmodhkrb2df1g80rusn1urnv43fi.apps.googleusercontent.com";
    private final String redirectURL = "http://localhost:8080/login/oauth2/code/google";

    public RedirectUrlDto getRedirectUrlDto(String ProviedType) {
        String url = authorizeURL + "?client_id=" + clientID + "&redirect_uri="
                + redirectURL + "&scope=https://www.googleapis.com/auth/indexing&response_type=code";
        return new RedirectUrlDto(url, ProviedType);
    }
    public Object login(LoginRequest request) {
        return null;
    }
}
