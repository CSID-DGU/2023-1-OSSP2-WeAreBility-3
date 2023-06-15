package com.dongguk.cse.naemansan.event;

import com.dongguk.cse.naemansan.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    @Async
    @EventListener
    public void sendNotificationForComment(CommentNotificationEvent event) throws Exception {
        notificationService.sendPushNotification(event.getFromUserId(), event.getToUserId(), event.getCourseId(), 1); // Service 연결
    }

    @Async
    @EventListener
    public void sendNotificationForLike(LikeNotificationEvent event) throws Exception {
        notificationService.sendPushNotification(event.getFromUserId(), event.getToUserId(), event.getCourseId(), 2);
    }

    @Async
    @EventListener
    public void sendNotificationForFollow(FollowNotificationEvent event) throws Exception {
        notificationService.sendPushNotificationForFollow(event.getFromUserId(), event.getToUserId(), 3);
    }
}
