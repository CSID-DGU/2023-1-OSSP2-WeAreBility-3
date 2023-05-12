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

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public Boolean createNotification(Long userId, NotificationDto notificationDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.error("Not Exist User - UserID: {}", userId);
            return Boolean.FALSE;
        }
        notificationRepository.save(Notification.builder()
                .user_id(userId)
                .content(notificationDto.getContent())
                .build());
        return Boolean.TRUE;
    }

    public List<NotificationDto> readNotification(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.error("Not Exist User - UserID: {}", userId);
            return null;
        }

        List<Notification> notifications = notificationRepository.findByUserId(userId);
        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            notificationDtos.add(NotificationDto.builder()
                    .id(notification.getId())
                    .user_id(notification.getUser_id())
                    .content(notification.getContent())
                    .create_date(notification.getCreate_date())
                    .is_read_status(notification.getIs_read_status()).build());
        }

        return notificationDtos;
    }

    public Boolean updateNotification(Long userId, Long notificationId, NotificationRequestDto notificationRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.error("Not Exist User - UserID: {}", userId);
            return Boolean.FALSE;
        }

        Optional<Notification> notification = notificationRepository.findByUserIDAndId(userId, notificationId);
        if (notification.isEmpty()) {
            log.error("Not Exist Notification - NotificationID: {}", notificationId);
            return Boolean.FALSE;
        }

        notification.get().setIs_read_status(notificationRequestDto.getIs_read_status());
        return Boolean.TRUE;
    }

    public Boolean deleteNotification(Long userId, Long notificationId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.error("Not Exist User - UserID: {}", userId);
            return Boolean.FALSE;
        }

        Optional<Notification> notification = notificationRepository.findByUserIDAndId(userId, notificationId);
        if (notification.isEmpty()) {
            log.error("Not Exist Notification - NotificationID: {}", notificationId);
            return Boolean.FALSE;
        }

        notificationRepository.deleteById(notificationId);
        return Boolean.TRUE;
    }
}
