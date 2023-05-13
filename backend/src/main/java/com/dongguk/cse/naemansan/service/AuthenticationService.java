package com.dongguk.cse.naemansan.service;

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
        log.info("유저 로그인 시작 Oauth: {}, 인가코드: {}", loginProviderType, authorizationCode);

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
            log.error("유저 정보를 들고 오지 못했습니다. - {}", loginProviderType);
            return null;
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
                    .useId(loginUser.getId())
                    .imageUseType(ImageUseType.USER)
                    .originName("default_image.png")
                    .uuidName("ca9ee169-6ff4-4ff9-87d4-bc7675eb91ca_default_image.png")
                    .type("image/png")
                    .path("C:/Users/HyungJoon/Documents/0_OSSP/resources/images/ca9ee169-6ff4-4ff9-87d4-bc7675eb91ca_default_image.png").build());
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

    public void logout(Long userId) {
        Optional<User> user =  userRepository.findById(userId);

        if (user.isEmpty()) {
            log.error("존재하지 않은 유저입니다. UserID: {}", userId);
            return;
        }

        Optional<RefreshToken> refreshToken = tokenRepository.findByUserId(userId);
        refreshToken.get().setRefreshToken(null);
    }

    public void withdrawal(Long userId) {
        Optional<User> user =  userRepository.findById(userId);

        if (user.isEmpty()) {
            log.error("존재하지 않은 유저입니다. UserID: {}", userId);
            return;
        }

        userRepository.delete(user.get());
    }
}
