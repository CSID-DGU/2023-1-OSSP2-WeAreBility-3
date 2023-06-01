package com.dongguk.cse.naemansan.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class NoticeDetailDto {
    private Long id;
    private String title;
    private String content;
    private Timestamp created_date;
    private Long read_cnt;
    private Boolean is_edit;

    @Builder
    public NoticeDetailDto(Long id, String title, String content, Timestamp created_date, Boolean is_edit, Long read_cnt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created_date = created_date;
        this.is_edit = is_edit;
        this.read_cnt = read_cnt;
    }
}
