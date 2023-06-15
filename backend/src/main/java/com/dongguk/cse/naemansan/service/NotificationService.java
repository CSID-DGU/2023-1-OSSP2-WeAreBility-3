package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.Comment;
import com.dongguk.cse.naemansan.domain.EnrollmentCourse;
import com.dongguk.cse.naemansan.domain.Notification;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.dto.NotificationDto;
import com.dongguk.cse.naemansan.dto.request.FCMNotificationRequestDto;
import com.dongguk.cse.naemansan.event.CommentNotificationEvent;
import com.dongguk.cse.naemansan.event.FollowNotificationEvent;
import com.dongguk.cse.naemansan.event.LikeNotificationEvent;
import com.dongguk.cse.naemansan.repository.EnrollmentCourseRepository;
import com.dongguk.cse.naemansan.repository.NotificationRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import com.dongguk.cse.naemansan.util.NotificationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final UserRepository userRepository;
    private final EnrollmentCourseRepository courseRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationUtil notificationUtil;

    public List<NotificationDto> readNotification(Long userId, Long pageNum, Long num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        Pageable paging = PageRequest.of(pageNum.intValue(), num.intValue(), Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Notification> notifications = notificationRepository.findByUser(user, paging);

        List<NotificationDto> notificationDtoList = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationDtoList.add(NotificationDto.builder()
                    .id(notification.getId())
                    .title(notification.getTitle())
                    .content(notification.getContent())
                    .create_date(notification.getCreateDate())
                    .is_read_status(notification.getIsReadStatus()).build());
        }
        return notificationDtoList;
    }

    public Boolean updateNotification(Long userId, Long notificationId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_NOTIFICATION));

        if (user.getId() != notification.getUser().getId()) {
            throw new RestApiException(ErrorCode.NOT_EQUAL);
        }

        notification.setIsReadStatus(Boolean.TRUE);
        return Boolean.TRUE;
    }

    public Boolean deleteNotification(Long userId, Long notificationId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_NOTIFICATION));

        if (user.getId() != notification.getUser().getId()) {
            throw new RestApiException(ErrorCode.NOT_EQUAL);
        }

        notificationRepository.delete(notification);
        return Boolean.TRUE;
    }

    public void sendPushNotification(Long fromUserId, Long toUserId, Long courseId, int NotificationType) throws Exception {
        User fromUser = userRepository.findById(fromUserId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        User toUser = userRepository.findById(toUserId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        EnrollmentCourse course = courseRepository.findByIdAndStatus(courseId, true).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ENROLLMENT_COURSE));
        FCMNotificationRequestDto fcmNotificationDto;
        String title = "내만산";
        String content = "오랜만에 산책 어떠신가요?";

        if (NotificationType == 1)   //댓글
            content = fromUser.getName() + "님이 산책로" + course.getTitle() + "에 댓글을 작성하였습니다.";
        else if (NotificationType == 2)  //좋아요
            content = fromUser.getName() + "님이 산책로" + course.getTitle() + "에 좋아요를 눌렀습니다.";


        if (toUser.getIsIos()) { //ios 푸시알림
            fcmNotificationDto = FCMNotificationRequestDto.builder()
                    .targetUserId(toUserId)
                    .title(title)
                    .body(content).build();
            notificationUtil.sendApnFcmtoken(fcmNotificationDto);
        } else { //안드로이드 푸시알림
            fcmNotificationDto = FCMNotificationRequestDto.builder()
                    .targetUserId(toUserId)
                    .title(title)
                    .body(content).build();
            notificationUtil.sendNotificationByToken(fcmNotificationDto);
            //notificationUtil.sendMessageTo(fcmNotificationDto); //버전2
        }
    }

    public void sendPushNotificationForFollow(Long fromUserId, Long toUserId, int NotificationType) throws Exception {
        User fromUser = userRepository.findById(fromUserId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        User toUser = userRepository.findById(toUserId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        FCMNotificationRequestDto fcmNotificationDto;
        String title = "내만산";
        String content = "오랜만에 산책 어떠신가요?";

        if (NotificationType == 3)  //팔로우
            content = fromUser.getName() + "님이 팔로우를 하였습니다";

        if (toUser.getIsIos()) { //ios 푸시알림
            fcmNotificationDto = FCMNotificationRequestDto.builder()
                    .targetUserId(toUserId)
                    .title(title)
                    .body(content).build();
            notificationUtil.sendApnFcmtoken(fcmNotificationDto);
        } else { //안드로이드 푸시알림
            fcmNotificationDto = FCMNotificationRequestDto.builder()
                    .targetUserId(toUserId)
                    .title(title)
                    .body(content).build();
            //notificationUtil.sendNotificationByToken(fcmNotificationDto);
            notificationUtil.sendMessageTo(fcmNotificationDto); //버전2
        }
    }
}
