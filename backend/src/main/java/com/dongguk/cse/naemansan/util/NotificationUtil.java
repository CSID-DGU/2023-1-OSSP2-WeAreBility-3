package com.dongguk.cse.naemansan.util;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.dto.MessageDto;
import com.dongguk.cse.naemansan.dto.request.FCMNotificationRequestDto;
import com.dongguk.cse.naemansan.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.JsonParseException;
import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NotificationUtil {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    //ios 푸시알림
    public void sendApnFcmtoken(FCMNotificationRequestDto requestDto) throws Exception {
        User user = userRepository.findById(requestDto.getTargetUserId()).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        if (user.getDeviceToken() != null) {
            try {
                PushNotificationPayload payload = PushNotificationPayload.complex();
                payload.addAlert(requestDto.getTitle());
                payload.getPayload().put("message", requestDto.getBody());
                payload.addBadge(1);
                payload.addSound("default");
                payload.addCustomDictionary("id", "1");
                System.out.println(payload.toString());
                Object obj = user.getDeviceToken();
                ClassPathResource resource = new ClassPathResource("SpringPushNotification.p12");
                //List<PushedNotification> NOTIFICATIONS = Push.payload(payload, "C:\Users\woobi\Documents\WeAreBility\2023-1-OSSP2-WeAreBility-3\backend\src\main\resources\\Certificates.p12", "naemansan@", false, obj);
                List<PushedNotification> NOTIFICATIONS = Push.payload(payload, resource.getPath(), "naemansan@", false, obj);
                for (PushedNotification NOTIFICATION : NOTIFICATIONS) {
                    if (NOTIFICATION.isSuccessful()) {
                        log.info("PUSH NOTIFICATION SENT SUCCESSFULLY TO" + NOTIFICATION.getDevice().getToken());
                    } else {
                        //부적절한 토큰 DB에서 삭제하기
                        Exception THEPROBLEM = NOTIFICATION.getException();
                        THEPROBLEM.printStackTrace();
                        ResponsePacket THEERRORRESPONSE = NOTIFICATION.getResponse();
                        if (THEERRORRESPONSE != null) {
                            log.info(THEERRORRESPONSE.getMessage());
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
            log.info("알림을 성공적으로 전송했습니다. targetUserID=" + requestDto.getTargetUserId());
        } else {
            log.info("서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserID=" + requestDto.getTargetUserId());
        }
    }

    //ios 테스트
    public void sendApnFcmtokenTest(String token) throws Exception {

        try {
            PushNotificationPayload payload = PushNotificationPayload.complex();
            payload.addAlert("title");
            payload.getPayload().put("message", "body");
            payload.addBadge(1);
            payload.addSound("default");
            payload.addCustomDictionary("id", "1");
            System.out.println(payload.toString());
            Object obj = token;
            ClassPathResource resource = new ClassPathResource("SpringPushNotification.p12");
            List<PushedNotification> NOTIFICATIONS = Push.payload(payload, "C:\\Users\\woobi\\Documents\\WeAreBility\\2023-1-OSSP2-WeAreBility-3\\backend\\src\\main\\resources\\Certificates.p12", "naemansan@", false, obj);
            //List<PushedNotification> NOTIFICATIONS = Push.payload(payload, resource.getPath(), "naemansan@", false, obj);
            for (PushedNotification NOTIFICATION : NOTIFICATIONS) {
                if (NOTIFICATION.isSuccessful()) {
                    log.info("PUSH NOTIFICATION SENT SUCCESSFULLY TO" + NOTIFICATION.getDevice().getToken());
                } else {
                    //부적절한 토큰 DB에서 삭제하기
                    Exception THEPROBLEM = NOTIFICATION.getException();
                    THEPROBLEM.printStackTrace();
                    ResponsePacket THEERRORRESPONSE = NOTIFICATION.getResponse();
                    if (THEERRORRESPONSE != null) {
                        log.info(THEERRORRESPONSE.getMessage());
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
    }

    //안드로이드 푸시알림
    public void sendNotificationByToken(FCMNotificationRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getTargetUserId()).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

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
                log.info("알림을 성공적으로 전송했습니다. targetUserID=" + requestDto.getTargetUserId());
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
                log.info("알림 보내기를 실패하였습니다. targetUserID=" + requestDto.getTargetUserId());
            }
        } else {
            log.info("서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserID=" + requestDto.getTargetUserId());
        }
    }

    //안드로이드 테스트
    public void sendNotificationByTokenTest(String token, String title, String body) {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();

            try {
                firebaseMessaging.send(message);
                log.info("알림을 성공적으로 전송했습니다.");
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
                log.info("알림 보내기를 실패하였습니다");
            }
    }

    //안드로이드 푸시알림 버전2
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/wearebility-303e9/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessageTo(FCMNotificationRequestDto requestDto) throws IOException {
        User user = userRepository.findById(requestDto.getTargetUserId()).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        if (user.getDeviceToken() != null) {
            String message = makeMessage(user.getDeviceToken(), requestDto.getTitle(), requestDto.getBody());
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();
            Response response = client.newCall(request).execute();
            log.info(response.body().string());
        } else {
            log.info("서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserID=" + requestDto.getTargetUserId());
        }
    }
    //안드로이드 버전2 테스트
    public ResponseEntity sendMessageToTest(String token, String title, String body) throws IOException {
        System.out.println("시작");
        String message = makeMessage(token, title, body);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();
        System.out.println("1");
        Response response = client.newCall(request).execute();
        System.out.println("2");
        System.out.println(response.body().string());
        System.out.println("3");
        //log.info(response.body().string());
        System.out.println("끝");
        return ResponseEntity.ok().build();
    }

    private String makeMessage(String targetToken, String title, String body) throws
            JsonParseException, JsonProcessingException {
        System.out.println("send시작");
        MessageDto messageDto = MessageDto.builder()
                .message(MessageDto.Message.builder()
                        .token(targetToken)
                        .notification(MessageDto.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();
        System.out.println("send끝");
        return objectMapper.writeValueAsString(messageDto);
    }

    private String getAccessToken() throws IOException {
        System.out.println("token시작");
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        System.out.println("token끝");
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
