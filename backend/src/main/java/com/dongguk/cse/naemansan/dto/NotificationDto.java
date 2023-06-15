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
    private Timestamp create_date;
    private Boolean is_read_status;
}

