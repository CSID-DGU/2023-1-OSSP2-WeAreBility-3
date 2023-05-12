package com.dongguk.cse.naemansan.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class NotificationDto {
    private Long id;
    private Long user_id;
    private String content;
    private Timestamp create_date;
    private Boolean is_read_status;

    @Builder
    public  NotificationDto(Long id, Long user_id, String content, Timestamp create_date, Boolean is_read_status){
        this.id = id;
        this.user_id = user_id;
        this.content = content;
        this.create_date = create_date;
        this.is_read_status = is_read_status;
    }
}
