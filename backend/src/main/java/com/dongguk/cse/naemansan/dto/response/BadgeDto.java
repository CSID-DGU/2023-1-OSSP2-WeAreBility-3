package com.dongguk.cse.naemansan.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
@Getter
public class BadgeDto {
    private Long badgeId;
    private String badgeName;
    private Timestamp getDate;

    @Builder
    public BadgeDto(Long badgeId, String badgeName, Timestamp getDate) {
        this.badgeId = badgeId;
        this.badgeName = badgeName;
        this.getDate = getDate;
    }
}
