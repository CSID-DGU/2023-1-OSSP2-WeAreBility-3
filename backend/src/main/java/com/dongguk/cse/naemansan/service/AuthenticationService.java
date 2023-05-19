package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.domain.type.ImageUseType;
import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import com.dongguk.cse.naemansan.dto.response.LoginResponse;
import com.dongguk.cse.naemansan.repository.ImageRepository;
import com.dongguk.cse.naemansan.repository.TokenRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import com.dongguk.cse.naemansan.security.jwt.JwtProvider;
import com.dongguk.cse.naemansan.security.jwt.JwtToken;
import com.dongguk.cse.naemansan.util.Oauth2Util;
import com.google.api.client.util.Value;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final ImageRepository imageRepository;
    private final JwtProvider jwtProvider;
    private final Oauth2Util oauth2Util;

    @Value("${spring.image.path: aaa.bbb.ccc}")
    private String FOLDER_PATH;

    public String getRedirectUrl(LoginProviderType loginProviderType) {
        switch (loginProviderType) {
            case KAKAO -> {
                return oauth2Util.getKakaoRedirectUrl();
            }
            case GOOGLE -> {
                return oauth2Util.getGoogleRedirectUrl();
            }
            case APPLE -> {
            }
        }
        return null;
    }
    public LoginResponse login(String authorizationCode, LoginProviderType loginProviderType) {
        String accessToken = null;
        String socialId = null;
        switch (loginProviderType) {
            case KAKAO -> {
                accessToken = oauth2Util.getKakaoAccessToken(authorizationCode);
                socialId = oauth2Util.getKakaoUserInformation(accessToken);
            }
            case GOOGLE -> {
                accessToken = oauth2Util.getGoogleAccessToken(authorizationCode);
                socialId = oauth2Util.getGoogleUserInformation(accessToken);
            }
            case APPLE -> {
            }
        }

        if (socialId == null) {
            throw new RestApiException(ErrorCode.NOT_FOUND_USER);
        }

        Random random = new Random();
        String userName = loginProviderType.toString() + "-";
        for (int i = 0; i < 3; i++) {
            userName += String.format("%04d", random.nextInt(1000));
        }


        Optional<User> user = userRepository.findBySocialLoginIdAndLoginProviderType(socialId, loginProviderType);
        User loginUser;

        if (user.isEmpty()) {
            loginUser = userRepository.save(User.builder()
                    .socialLoginId(socialId)
                    .name(userName)
                    .loginProviderType(loginProviderType)
                    .build());
            imageRepository.save(Image.builder()
                    .userObject(loginUser)
                    .imageUseType(ImageUseType.USER)
                    .originName("default_image.png")
                    .uuidName("0_default_image.png")
                    .type("image/png")
                    .path(FOLDER_PATH + "0_default_image.png").build());
        } else {
            loginUser = user.get();
        }

        JwtToken jwtToken = jwtProvider.createTotalToken(loginUser.getId(), loginUser.getUserRoleType());

        Optional<Token> refreshToken = tokenRepository.findByTokenUser(loginUser);

        if (refreshToken.isEmpty()) {
            tokenRepository.save(Token.builder()
                    .tokenUser(loginUser)
                    .refreshToken(jwtToken.getRefreshToken())
                    .build());
        } else {
            refreshToken.get().setRefreshToken(jwtToken.getRefreshToken());
        }

        return LoginResponse.builder()
                .jwt(jwtToken)
                .build();
    }

    public void logout(Long userId) {
        User user =  userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        user.getToken().setRefreshToken(null);
    }

    public void withdrawal(Long userId) {
        User user =  userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        userRepository.delete(user);
    }
}
