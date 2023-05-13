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

    //Notification Read
    @GetMapping("")
    public ResponseDto<List<NotificationDto>> readNotification(Authentication authentication) {
        return new ResponseDto<List<NotificationDto>>(notificationService.readNotification(Long.valueOf(authentication.getName())));
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
