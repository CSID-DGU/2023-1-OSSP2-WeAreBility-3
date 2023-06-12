package com.dongguk.cse.naemansan.service;


import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.dto.request.FCMNotificationRequestDto;
import com.dongguk.cse.naemansan.repository.UserRepository;
import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Service
public class IosNotificationService {
    private final UserRepository userRepository;

    public String sendApnFcmtoken(FCMNotificationRequestDto requestDto) throws Exception{
        User user = userRepository.findById(requestDto.getTargetUserId()).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        if (user.getDeviceToken() != null) {
            try {
                PushNotificationPayload payload = PushNotificationPayload.complex();
                payload.addAlert("푸시알림 테스트");
                payload.addBadge(1);
                payload.addSound("default");
                payload.addCustomDictionary("id", "1");
                System.out.println(payload.toString());
                Object obj = user.getDeviceToken();
                InputStream inputStream = new ClassPathResource("SpringPushNotification.p12").getInputStream();

                List<PushedNotification> NOTIFICATIONS = Push.payload(payload, "경로", "비밀번호", false, obj);
                for (PushedNotification NOTIFICATION : NOTIFICATIONS) {
                    if (NOTIFICATION.isSuccessful()) {
                        System.out.println("PUSH NOTIFICATION SENT SUCCESSFULLY TO" + NOTIFICATION.getDevice().getToken());
                    } else {
                        //부적절한 토큰 DB에서 삭제하기
                        Exception THEPROBLEM = NOTIFICATION.getException();
                        THEPROBLEM.printStackTrace();
                        ResponsePacket THEERRORRESPONSE = NOTIFICATION.getResponse();
                        if (THEERRORRESPONSE != null) {
                            System.out.println(THEERRORRESPONSE.getMessage());
                        }
                    }
                }
            } catch (KeystoreException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (CommunicationException e) {
                e.printStackTrace();
            }
            return "알림을 성공적으로 전송했습니다. targetUserID=" + requestDto.getTargetUserId();
        } else {
            return "서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserID=" + requestDto.getTargetUserId();
        }
    }
}
