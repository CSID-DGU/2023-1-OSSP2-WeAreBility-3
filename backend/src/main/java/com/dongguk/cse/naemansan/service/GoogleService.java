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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
}
