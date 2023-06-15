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

import java.io.IOException;
import java.util.List;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class NotificationUtil {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    //ios 푸시알림
    public void/*8tring*/ sendApnFcmtoken(/*FCMNotificationRequestDto requestDto*/String token) throws Exception{
        //User user = userRepository.findById(requestDto.getTargetUserId()).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        //if (user.getDeviceToken() != null) {
        try {
            PushNotificationPayload payload = PushNotificationPayload.complex();
            payload.addAlert("푸시알림 테스트");
            payload.addBadge(1);
            payload.addSound("default");
            payload.addCustomDictionary("id", "1");
            System.out.println(payload.toString());
            //Object obj = user.getDeviceToken();
            Object obj = token;
            //InputStream inputStream = new ClassPathResource("SpringPushNotification.p12").getInputStream();

            List<PushedNotification> NOTIFICATIONS = Push.payload(payload, "C:\\Certificates.p12", "naemansan@", false, obj);
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
        //return "알림을 성공적으로 전송했습니다. targetUserID=" + requestDto.getTargetUserId();
        //} else {
        //     return "서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserID=" + requestDto.getTargetUserId();
    }

    //안드로이드 푸시알림
    public String sendNotificationByToken(FCMNotificationRequestDto requestDto) {
        //Optional<User> user = userRepository.findById(requestDto.getTargetUserId());
        User user = userRepository.findById(requestDto.getTargetUserId()).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        //if (user.isPresent()) {
        if (user.getDeviceToken() != null) {
            com.google.firebase.messaging.Notification notification = com.google.firebase.messaging.Notification.builder()
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

    //안드로이드 푸시알림 버전2
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/wearebility-303e9/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        //System.out.println("sendMessageTo 시작");
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        //System.out.println("sendMessageTo 끝나기 전");
        System.out.println(response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        //System.out.println("makeMessage 시작");

        MessageDto messageDto = MessageDto.builder()
                .message(MessageDto.Message.builder()
                        .token(targetToken)
                        .notification(MessageDto.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();


        System.out.println(targetToken + " / " + title + " / " + body);
        //System.out.println("makeMessage 끝");
        return objectMapper.writeValueAsString(messageDto);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}