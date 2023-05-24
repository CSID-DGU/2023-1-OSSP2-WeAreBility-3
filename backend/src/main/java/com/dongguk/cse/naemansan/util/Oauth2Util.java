package com.dongguk.cse.naemansan.util;

import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2Util {
    // KAKAO 용 Data
    @Value("${client.provider.kakao.authorization-uri: aaa.bbb.ccc}")
    private String kakaoAuthorizationUrl;
    @Value("${client.provider.kakao.token-uri: aaa.bbb.ccc}")
    private String kakaoTokenUrl;
    @Value("${client.provider.kakao.user-info-uri: aaa.bbb.ccc}")
    private String kakaoUserInfoUrl;
    @Value("${client.registration.kakao.client-id: aaa.bbb.ccc}")
    private String kakaoClientId;
    @Value("${client.registration.kakao.client-secret: aaa.bbb.ccc}")
    private String kakaoClientSecret;
    @Value("${client.registration.kakao.redirect-uri: aaa.bbb.ccc}")
    private String kakaoRedirectURL;

    // GOOGLE 용 Data
    @Value("${client.provider.google.authorization-uri: aaa.bbb.ccc}")
    private String GoogleAuthorizationUrlL;
    @Value("${client.provider.google.token-uri: aaa.bbb.ccc}")
    private String GoogleTokenUrl;
    @Value("${client.provider.google.user-info-uri: aaa.bbb.ccc}")
    private String GoogleUserInfoUrl;
    @Value("${client.registration.google.client-id: aaa.bbb.ccc}")
    private String GoogleClientId;
    @Value("${client.registration.google.client-secret: aaa.bbb.ccc}")
    private String GoogleClientSecret;
    @Value("${client.registration.google.redirect-uri: aaa.bbb.ccc}")
    private String GoogleRedirectURL;

    private static final RestTemplate restTemplate = new RestTemplate();

    public String getKakaoRedirectUrl() {
        String url = kakaoAuthorizationUrl
                + "?client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectURL
                + "&response_type=code";
        return url;
    }

    public String getGoogleRedirectUrl() {
        String url = GoogleAuthorizationUrlL
                + "?client_id=" + GoogleClientId
                + "&redirect_uri=" + GoogleRedirectURL
                + "&response_type=code&scope=https://www.googleapis.com/auth/userinfo.profile";
        return url;
    }

    public String getKakaoAccessToken(String authorizationCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("client_secret", kakaoClientSecret);
        params.add("redirect_uri", kakaoRedirectURL);
        params.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(params,httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                kakaoTokenUrl,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        return JsonParser.parseString(response.getBody()).getAsJsonObject().get("access_token").getAsString();
    }

    public String getGoogleAccessToken(String authorizationCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id", GoogleClientId);
        params.add("client_secret", GoogleClientSecret);
        params.add("redirect_uri", GoogleRedirectURL);
        params.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String,String>> googleTokenRequest = new HttpEntity<>(params,httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                GoogleTokenUrl,
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );

        return JsonParser.parseString(response.getBody()).getAsJsonObject().get("access_token").getAsString();
    }

    public String getKakaoUserInformation(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Bearer "+ accessToken);
        httpHeaders.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String,String >> kakaoProfileRequest= new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                kakaoUserInfoUrl,
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        // 토큰을 사용하여 사용자 정보 추출
        JsonElement element = JsonParser.parseString(response.getBody());

        return element.getAsJsonObject().get("id").getAsString();
    }

    public String getGoogleUserInformation(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Bearer "+ accessToken);
        httpHeaders.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> googleProfileRequest= new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                GoogleUserInfoUrl,
                HttpMethod.GET,
                googleProfileRequest,
                String.class
        );

        // 토큰을 사용하여 사용자 정보 추출
        JsonElement element = JsonParser.parseString(response.getBody());

        return element.getAsJsonObject().get("id").getAsString();
    }
}
