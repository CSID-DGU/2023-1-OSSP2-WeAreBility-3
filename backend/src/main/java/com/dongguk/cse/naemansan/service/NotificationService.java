package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Notification;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.dto.NotificationDto;
import com.dongguk.cse.naemansan.dto.request.NotificationRequestDto;
import com.dongguk.cse.naemansan.repository.NotificationRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    //title 처리 해야함
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    public Boolean createNotification(Long userId, NotificationDto notificationDto, NotificationRequestDto notificationRequestDto) throws IOException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.error("Not Exist User - UserID: {}", userId);
            return Boolean.FALSE;
        }
        notificationRepository.save(Notification.builder()
                .notificationUser(user.get())
                .content(notificationDto.getMessage().getNotification().getContent())
                .build());
        //알림 보내기 추가 sendMessageTo 추가, makeMessage 추가
        firebaseCloudMessageService.sendMessageTo(
                notificationRequestDto.getTargetToken(),
                notificationRequestDto.getTitle(),
                notificationRequestDto.getContent());
        return Boolean.TRUE;
    }

    public List<NotificationDto> readNotification(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.error("Not Exist User - UserID: {}", userId);
            return null;
        }

        List<Notification> notifications = notificationRepository.findByNotificationUser(user.get());
        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            notificationDtos.add(NotificationDto.builder()
                    .id(notification.getId())
                    .message(NotificationDto.Message.builder()
                            .notification(NotificationDto.Notification.builder()
                                    .content(notification.getContent())
                                    .build()).build())
                    .createDate(notification.getCreateDate())
                    .isReadStatus(notification.getIsReadStatus()).build());
        }

        return notificationDtos;
    }

    public Boolean updateNotification(Long userId, Long notificationId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.error("Not Exist User - UserID: {}", userId);
            return Boolean.FALSE;
        }

        Optional<Notification> notification = notificationRepository.findByIdAndNotificationUser(notificationId, user.get());
        if (notification.isEmpty()) {
            log.error("Not Exist Notification - NotificationID: {}", notificationId);
            return Boolean.FALSE;
        }

        notification.get().setIsReadStatus(Boolean.TRUE);
        return Boolean.TRUE;
    }

    public Boolean deleteNotification(Long userId, Long notificationId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.error("Not Exist User - UserID: {}", userId);
            return Boolean.FALSE;
        }

        Optional<Notification> notification = notificationRepository.findByIdAndNotificationUser(notificationId, user.get());
        if (notification.isEmpty()) {
            log.error("Not Exist Notification - NotificationID: {}", notificationId);
            return Boolean.FALSE;
        }

        notificationRepository.deleteById(notificationId);
        return Boolean.TRUE;
    }
}
