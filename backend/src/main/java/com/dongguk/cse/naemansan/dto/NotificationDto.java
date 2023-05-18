package com.dongguk.cse.naemansan.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor //추가
@Getter
public class NotificationDto {
    private Long id;
    private Timestamp createDate;
    private Boolean isReadStatus;
    private boolean validateOnly; //추가
    private Message message; //추가

    //추가
    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;
        private String token;
    }

    //추가
    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String content; //수정
        private String image;
    }

    @Builder
    public NotificationDto(Long id, Timestamp createDate, Boolean isReadStatus) {
        this.id = id;
        // this.content = content;
        this.createDate = createDate;
        this.isReadStatus = isReadStatus;
    }

}
