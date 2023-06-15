package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.NotificationDto;
import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.dto.request.FCMNotificationRequestDto;
import com.dongguk.cse.naemansan.dto.request.NotificationRequestDto;
import com.dongguk.cse.naemansan.service.NotificationService;
import com.dongguk.cse.naemansan.util.NotificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationUtil notificationUtil;

    //안드로이드 푸시알림 테스트
    @PostMapping("/andfcm")
    public void sendNotificationByToken(@RequestBody NotificationRequestDto requestDto) throws IOException {
        notificationUtil.sendNotificationByTokenTest(requestDto.getTargetToken(), requestDto.getTitle(), requestDto.getBody());
    }

    //안드로이드 버전2 테스트
    @PostMapping("/andfcm2")
    public void pushMessage(@RequestBody NotificationRequestDto requestDto) throws IOException {
        System.out.println(requestDto.getTargetToken() + " / " + requestDto.getTitle() + " / " + requestDto.getBody());
        notificationUtil.sendMessageToTest(requestDto.getTargetToken(), requestDto.getTitle(), requestDto.getBody());
    }

    //ios 푸시알림 테스트
    @PostMapping("/api/iosfcm")
    public void pushIosMessage(@RequestBody String token) throws Exception {
        notificationUtil.sendApnFcmtokenTest(token);
    }

    //Notification Read
    @GetMapping("")
    public ResponseDto<List<NotificationDto>> readNotification(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<NotificationDto>>(notificationService.readNotification(Long.valueOf(authentication.getName()), page, num));
    }

    //Notification Update
    @PutMapping("/{notificationId}")
    public ResponseDto<Boolean> updateNotification(Authentication authentication, @PathVariable Long notificationId) {
        return new ResponseDto<Boolean>(notificationService.updateNotification(Long.valueOf(authentication.getName()), notificationId));
    }

    //Notification Delete
    @DeleteMapping("/{notificationId}")
    public ResponseDto<Boolean> deleteNotification(Authentication authentication, @PathVariable Long notificationId) {
        return new ResponseDto<Boolean>(notificationService.deleteNotification(Long.valueOf(authentication.getName()), notificationId));
    }
}
