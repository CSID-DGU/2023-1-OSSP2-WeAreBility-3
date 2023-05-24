package com.dongguk.cse.naemansan.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
@Getter
public class BadgeDto {
    private Long badge_id;
    private String badge_name;
    private Timestamp get_date;

    @Builder
    public BadgeDto(Long badge_id, String badge_name, Timestamp get_date) {
        this.badge_id = badge_id;
        this.badge_name = badge_name;
        this.get_date = get_date;
    }
}
