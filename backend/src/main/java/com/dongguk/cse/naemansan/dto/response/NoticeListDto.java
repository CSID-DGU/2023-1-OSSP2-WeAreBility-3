package com.dongguk.cse.naemansan.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class NoticeListDto {
    private Long id;
    private String title;
    private Timestamp created_date;
    private Long read_cnt;

    @Builder
    public NoticeListDto(Long id, String title, Timestamp created_date, Long read_cnt) {
        this.id = id;
        this.title = title;
        this.created_date = created_date;
        this.read_cnt = read_cnt;
    }
}
