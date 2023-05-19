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
    private String title;
    private String content;
    private Timestamp createDate;
    private Boolean isReadStatus;

/*
    @Builder
    public NotificationDto(Long id, String title, String content, Timestamp createDate, Boolean isReadStatus) {
        this.id = id;
        this.title=title;
        this.content=content;
        this.createDate = createDate;
        this.isReadStatus = isReadStatus;
    }*/
}

