package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.domain.type.ImageUseType;
import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import com.dongguk.cse.naemansan.dto.RedirectUrlDto;
import com.dongguk.cse.naemansan.repository.ImageRepository;
import com.dongguk.cse.naemansan.repository.TokenRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import com.dongguk.cse.naemansan.security.jwt.JwtProvider;
import com.dongguk.cse.naemansan.security.jwt.JwtToken;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleService implements AuthenticationService {
    // Google 용 Data
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

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    public RedirectUrlDto getRedirectUrlDto(String ProviedType) {
        String url = GoogleAuthorizationUrlL
                + "?client_id=" + GoogleClientId
                + "&redirect_uri=" + GoogleRedirectURL
                + "&response_type=code&scope=https://www.googleapis.com/auth/userinfo.profile";
        return new RedirectUrlDto(url, ProviedType);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        LoginProviderType providerType = request.getProvider();
        Map<String, Object> loginUserInfo = new HashMap<>();

        System.out.println(providerType);

        String socialLoginId = null;
        String name = null;

        // 처음 로그인 용
        if (LoginProviderType.GOOGLE.equals(providerType)) {
            loginUserInfo = getUserInfo(getAccessToken(request.getCode()));
        }
        // 제공자가 아예 이상한 상황용
        else {
            throw new NullPointerException();
        }

        socialLoginId = loginUserInfo.get("id").toString();
        name = loginUserInfo.get("name").toString();

        Optional<User> user = userRepository.findBySocialLoginIdAndLoginProviderType(socialLoginId, providerType);
        User loginUser;

        if (user.isEmpty()) {
            loginUser = userRepository.save(User.builder()
                    .socialLoginId(socialLoginId)
                    .name(name)
                    .loginProviderType(providerType)
                    .build());
            imageRepository.save(Image.builder()
                    .useId(loginUser.getId())
                    .imageUseType(ImageUseType.USER)
                    .build());
        } else {
            loginUser = user.get();
        }

        JwtToken jwtToken = jwtProvider.createTotalToken(loginUser.getId(), loginUser.getUserRoleType());

        Optional<RefreshToken> refreshToken = tokenRepository.findByUserId(loginUser.getId());

        if (refreshToken.isEmpty()) {
            tokenRepository.save(RefreshToken.builder()
                    .userId(loginUser.getId())
                    .refreshToken(jwtToken.getRefreshToken())
                    .build());
        } else {
            refreshToken.get().setRefreshToken(jwtToken.getRefreshToken());
        }

        return LoginResponse.builder()
                .jwt(jwtToken)
                .build();
    }

    @Override
    public String getAccessToken(String code) {
        String accessToken = "";

        try {
            URL url = new URL(GoogleTokenUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + GoogleClientId);
            sb.append("&client_secret=" + GoogleClientSecret);
            sb.append("&redirect_uri=" + GoogleRedirectURL);
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            System.out.println(result);

            JsonElement element = JsonParser.parseString(result);
            accessToken = element.getAsJsonObject().get("access_token").getAsString();

            br.close();
            bw.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    @Override
    public Map<String, Object> getUserInfo(String accessToken) {
        System.out.println(accessToken);
        Map<String, Object> userInfo = new HashMap<>();

        try {
            URL url = new URL(GoogleUserInfoUrl);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            System.out.println(result);

            JsonElement element = JsonParser.parseString(result);

            String id = element.getAsJsonObject().get("id").getAsString();
            String name = element.getAsJsonObject().get("given_name").getAsString();

            userInfo.put("id", id);
            userInfo.put("name", name);
            userInfo.put("provider", LoginProviderType.GOOGLE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    public LoginResponse testAccessToken(String code, HttpServletRequest request) {
        log.info("인가코드 - {}", code);
        RestTemplate rt = new RestTemplate();
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id", GoogleClientId);
        params.add("client_secret", GoogleClientSecret);
        params.add("redirect_uri", "http://localhost:8080/auth/google/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String,String>> googleTokenRequest = new HttpEntity<>(params,httpHeaders);

        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );
        // 토큰값 Json 형식으로 가져오기위해 생성
//        log.debug("kakao token result = {} " , response);
        log.info("접근토큰 - {}", JsonParser.parseString(response.getBody()).getAsJsonObject().get("access_token").getAsString());
        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();

        headers2.add("Authorization", "Bearer "+ JsonParser.parseString(response.getBody()).getAsJsonObject().get("access_token").getAsString());
        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2= new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://www.googleapis.com/userinfo/v2/me",
                HttpMethod.GET,
                kakaoProfileRequest2,
                String.class
        );

        // 토큰을 사용하여 사용자 정보 추출
        JsonElement element = JsonParser.parseString(response2.getBody());
        String socialLoginId = element.getAsJsonObject().get("id").getAsString();
        String name = element.getAsJsonObject().get("given_name").getAsString();

        // 이후 유저 여부를 판단하고 회원가입 / 로그인 처리를 진행하면 된다.
        Optional<User> user = userRepository.findBySocialLoginIdAndLoginProviderType(socialLoginId, LoginProviderType.GOOGLE);
        User loginUser;

        if (user.isEmpty()) {
            loginUser = userRepository.save(User.builder()
                    .socialLoginId(socialLoginId)
                    .name(name)
                    .loginProviderType(LoginProviderType.KAKAO)
                    .build());
            imageRepository.save(Image.builder()
                    .useId(loginUser.getId())
                    .imageUseType(ImageUseType.USER)
                    .build());
        } else {
            loginUser = user.get();
        }

        JwtToken jwtToken = jwtProvider.createTotalToken(loginUser.getId(), loginUser.getUserRoleType());

        Optional<RefreshToken> refreshToken = tokenRepository.findByUserId(loginUser.getId());

        if (refreshToken.isEmpty()) {
            tokenRepository.save(RefreshToken.builder()
                    .userId(loginUser.getId())
                    .refreshToken(jwtToken.getRefreshToken())
                    .build());
        } else {
            refreshToken.get().setRefreshToken(jwtToken.getRefreshToken());
        }

        return LoginResponse.builder()
                .jwt(jwtToken)
                .build();
    }
}
