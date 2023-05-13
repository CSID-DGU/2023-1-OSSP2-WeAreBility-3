package com.dongguk.cse.naemansan.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class NotificationDto {
    private Long id;
    private String content;
    private Timestamp createDate;
    private Boolean isReadStatus;

    @Builder
    public  NotificationDto(Long id, String content, Timestamp createDate, Boolean isReadStatus){
        this.id = id;
        this.content = content;
        this.createDate = createDate;
        this.isReadStatus = isReadStatus;
    }
}
