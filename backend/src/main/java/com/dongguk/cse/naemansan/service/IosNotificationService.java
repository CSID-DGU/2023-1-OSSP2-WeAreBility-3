package com.dongguk.cse.naemansan.service;


import javapns.Push;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import javax.naming.CommunicationException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class IosNotificationService {
    public static void sendApnFcmtoken(String token) {
        try {
            PushNotificationPayload payload = PushNotificationPayload.complex();
            payload.addAlert("푸시알림 테스트");
            payload.addBadge(1);
            payload.addSound("default");
            payload.addCustomDictionary("id", "1");
            System.out.println(payload.toString());
            Object obj = token;
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
        } catch (CommunicationException e) {
            e.printStackTrace();
        } catch (KeystoreException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
