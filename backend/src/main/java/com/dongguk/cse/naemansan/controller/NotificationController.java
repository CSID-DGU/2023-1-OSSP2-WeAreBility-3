package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.NotificationDto;
import com.dongguk.cse.naemansan.dto.ResponseDto;
import com.dongguk.cse.naemansan.dto.request.NotificationRequestDto;
import com.dongguk.cse.naemansan.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    //Notification Create
    @PostMapping("/{userId}/notification")
    public ResponseDto<Boolean> createNotification(Authentication authentication, @RequestBody NotificationDto notificationDto) {
        return new ResponseDto<Boolean>(notificationService.createNotification(Long.valueOf(authentication.getName()), notificationDto));
    }

    //Notification Read
    @GetMapping("/{userId}/notification")
    public ResponseDto<List<NotificationDto>> readNotification(@PathVariable Long userId) {
        return new ResponseDto<List<NotificationDto>>(notificationService.readNotification(userId));
    }

    //Notification Update
    @PutMapping("/{userId}/notification/{notificationId")
    public ResponseDto<Boolean> updateNotification(Authentication authentication, @PathVariable Long notificationId, @RequestBody NotificationRequestDto notificationRequestDto) {
        return new ResponseDto<Boolean>(notificationService.updateNotification(Long.valueOf(authentication.getName()), notificationId, notificationRequestDto));
    }

    //Notification Delete
    @DeleteMapping("/{userId}/notification/{notificationId}")
    public ResponseDto<Boolean> deleteNotification(Authentication authentication, @PathVariable Long notificationId) {
        return new ResponseDto<Boolean>(notificationService.deleteNotification(Long.valueOf(authentication.getName()), notificationId));
    }
}
