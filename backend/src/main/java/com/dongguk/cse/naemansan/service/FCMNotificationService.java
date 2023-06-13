package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.dto.request.FCMNotificationRequestDto;
import com.dongguk.cse.naemansan.dto.request.NotificationRequestDto;
import com.dongguk.cse.naemansan.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public String sendNotificationByToken(FCMNotificationRequestDto requestDto) {
        //Optional<User> user = userRepository.findById(requestDto.getTargetUserId());
        User user = userRepository.findById(requestDto.getTargetUserId()).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        //if (user.isPresent()) {
        if (user.getDeviceToken() != null) {
            Notification notification = Notification.builder()
                    .setTitle(requestDto.getTitle())
                    .setBody(requestDto.getBody())
                    .build();

            Message message = Message.builder()
                    .setToken(user.getDeviceToken())
                    .setNotification(notification)
                    .build();

            try {
                firebaseMessaging.send(message);
                return "알림을 성공적으로 전송했습니다. targetUserID=" + requestDto.getTargetUserId();
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
                return "알림 보내기를 실패하였습니다. targetUserID=" + requestDto.getTargetUserId();
            }
        } else {
            return "서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserID=" + requestDto.getTargetUserId();
        }
        //}else {
        //    return "해당 유저가 존재하지 않습니다. targetUserID="+requestDto.getTargetUserId();
        //}
    }
}
