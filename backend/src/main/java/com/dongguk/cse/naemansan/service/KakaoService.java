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
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoService implements AuthenticationService {
    // Kakao 용 Data
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
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final ImageRepository imageRepository;
    private final JwtProvider jwtProvider;

    public RedirectUrlDto getRedirectUrlDto(String ProviedType) {
        String url = kakaoAuthorizationUrl
                + "?client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectURL
                + "&response_type=code";
        return new RedirectUrlDto(url, ProviedType);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        LoginProviderType providerType = request.getProvider();
        Map<String, Object> loginUserInfo = new HashMap<>();

        System.out.println(providerType);

        String socialLoginId = null;
        String name = null;

        // 처음 로그인 용
        if (LoginProviderType.KAKAO.equals(providerType)) {
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
            URL url = new URL(kakaoTokenUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + kakaoClientId);
            sb.append("&client_secret=" + kakaoClientSecret);
            sb.append("&redirect_uri=" + kakaoRedirectURL);
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
        Map<String, Object> userInfo = new HashMap<String, Object>();

        try {
            URL url = new URL(kakaoUserInfoUrl);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonElement element = JsonParser.parseString(result);

            String id = element.getAsJsonObject().get("id").getAsString();
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();

            String name = properties.getAsJsonObject().get("nickname").getAsString();

            userInfo.put("id", id);
            userInfo.put("name", name);
            userInfo.put("provider", LoginProviderType.KAKAO);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }
}
