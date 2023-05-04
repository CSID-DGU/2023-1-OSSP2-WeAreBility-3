package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import com.dongguk.cse.naemansan.dto.RedirectUrlDto;
import com.dongguk.cse.naemansan.repository.UserRepository;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
public class KakaoService implements AuthService{
    private final String authorizeURL = "https://kauth.kakao.com/oauth/authorize";
    private final String tokenUrl = "https://kauth.kakao.com/oauth/token";
    private final String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
    private final String clientID = "db9bfe01c95414e0f1add469fdfaa9ff";
    private final String redirectURL = "http://localhost:8080/login/oauth2/code/kakao";
    private final String clinetSecret = "uH8Hae1LjyocJbxWsqGTNvhe09CeMuZw";

    private final UserRepository userRepository;

    public RedirectUrlDto getRedirectUrlDto(String ProviedType) {
        String url = authorizeURL + "?client_id=" + clientID + "&redirect_uri="
                + redirectURL + "&response_type=code";
        return new RedirectUrlDto(url, ProviedType);
    }
    public LoginResponse login(LoginRequest request) {
        LoginProviderType providerType = request.getProvider();
        Map<String, Object> loginUserInfo = new HashMap<>();

        System.out.println(providerType);

        String socialLoginId = null;
        String name = null;
        if (LoginProviderType.KAKAO.equals(providerType)) {
            loginUserInfo = getUserInfo(request.getCode());
        } else {
            throw new NullPointerException();
        }

        socialLoginId = loginUserInfo.get("id").toString();
        name = loginUserInfo.get("name").toString();

        Optional<User> user = userRepository.findBySocialLoginIdAndLoginProviderType(socialLoginId, providerType);
        User loginUser;

        if (user.isEmpty()) {
            loginUser = User.builder()
                    .socialLoginId(socialLoginId)
                    .name(name)
                    .loginProviderType(providerType)
                    .build();
            userRepository.save((loginUser));
        } else {
            loginUser = user.get();
        }
//
//        String token = jwtTokenProvider.createAccessToken(String.valueOf(loginUser.getId()));

        return LoginResponse.builder()
                .id(loginUser.getSocialLoginId())
                .name(loginUser.getName())
                .provider(loginUser.getLoginProviderType())
                .build();
    }

    @Override
    public Map<String, Object> getAccessToken(String code) {
        Map<String, Object> tokenMap = new HashMap<>();
        String accessToken = "";
        String refreshToken = "";

        try {
            URL url = new URL(tokenUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + clientID);
            sb.append("&client_secret=" + clinetSecret);
            sb.append("&redirect_uri=" + redirectURL);
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }


            JsonElement element = JsonParser.parseString(result);
            tokenMap.put("access_token", element.getAsJsonObject().get("access_token").getAsString());
            tokenMap.put("refresh_token", element.getAsJsonObject().get("refresh_token").getAsString());

            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokenMap;
    }

    @Override
    public Map<String, Object> getUserInfo(String code) {
        Map<String, Object> tokenMap = getAccessToken(code);
        Map<String, Object> userInfo = new HashMap<String, Object>();
        String accessToken = tokenMap.get("access_token").toString();
        String refreshToken = tokenMap.get("refresh_token").toString();

        try {
            URL url = new URL(userInfoUrl);

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

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();

            userInfo.put("id", id);
            userInfo.put("name", nickname);
            userInfo.put("provider", LoginProviderType.KAKAO);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    @Override
    public void logout(String accessToken) {

    }
}
