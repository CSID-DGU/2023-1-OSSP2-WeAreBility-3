package com.dongguk.cse.naemansan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
//삭제 예정
@Builder
@AllArgsConstructor
@Getter
public class MessageDto {
    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;
        private String token;
    }
    @Builder
    @AllArgsConstructor
    @Getter
    public static class  Notification{
        private String title;
        private String body;
        private String image;
    }
}
