package com.dongguk.cse.naemansan.event;

import com.dongguk.cse.naemansan.service.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BadgeEventListener {
    private final BadgeService badgeService;

    @Async
    @EventListener
    public void sendPushForIndividualBadge(IndividualCourseBadgeEvent event) throws Exception {
        badgeService.createIndividualBadge(event.getUserId());
    }

    @Async
    @EventListener
    public void sendPushForEnrollmentBadge(EnrollmentCourseBadgeEvent event) throws Exception {
        badgeService.createEnrollmentBadge(event.getUserId());
    }

    @Async
    @EventListener
    public void sendPushForUsingBadge(UsingCourseBadgeEvent event) throws InterruptedException {
        badgeService.createUsingBadge(event.getUserId());
    }

    @Async
    @EventListener
    public void sendPushForCommentBadge(CommentBadgeEvent event) throws InterruptedException {
        badgeService.createCommentBadge(event.getUserId());
    }
}