package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.dto.UserDto;
import com.dongguk.cse.naemansan.dto.request.UserRequestDto;
import com.dongguk.cse.naemansan.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final SubscribeRepository subscribeRepository;

    public UserDto readUserProfile(Long userId) {
        log.info("getUserInformation - ID : {}", userId);

        Optional<User> user = userRepository.findById(userId);
        Optional<Subscribe> subscribe = subscribeRepository.findBySubscribeUser(user.get());


        UserDto userDto = null;
        try {
            if (user.isEmpty())
                throw new NullPointerException();
            else
                userDto = UserDto.builder()
                        .user(user.get())
                        .image(user.get().getImage())
                        .isPremium(subscribe.isEmpty() ? false : true)
                        .commentCnt((long) user.get().getComments().size())
                        .likeCnt((long) user.get().getLikes().size())
                        .badgeCnt((long) user.get().getBadges().size())
                        .followingCnt((long) user.get().getFollowings().size())
                        .followerCnt((long) user.get().getFollowers().size())
                        .build();
        } catch (Exception e) {
            log.info("{}", e.getMessage());
        }

        return userDto;
    }

    @Transactional
    public UserDto updateUserProfile(Long userId, UserRequestDto userRequestDto) {
        log.info("updateUserInformation - {}", userRequestDto);
        Optional<User> user = userRepository.findById(userId);
        Optional<Image> image = imageRepository.findByImageUser(user.get());
        Optional<Subscribe> subscribe = subscribeRepository.findBySubscribeUser(user.get());
        if (user.isEmpty() || image.isEmpty()){
            return null;
        }
        else {
            user.get().updateUser(userRequestDto.getName(), userRequestDto.getInformation());
//            image.get().setImagePath(userRequestDto.getImagePath());
            // 이미지는 따로 한번 더 요청하는 것을 생각 중
            return UserDto.builder()
                    .user(user.get())
                    .image(user.get().getImage())
                    .isPremium(subscribe.isEmpty() ? false : true)
                    .commentCnt((long) user.get().getComments().size())
                    .likeCnt((long) user.get().getLikes().size())
                    .badgeCnt((long) user.get().getBadges().size())
                    .followingCnt((long) user.get().getFollowings().size())
                    .followerCnt((long) user.get().getFollowers().size())
                    .build();
        }
    }

    public Boolean deleteUserInformation(Long id) {
        try {
            userRepository.deleteById(id);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return Boolean.FALSE;
    }
}
