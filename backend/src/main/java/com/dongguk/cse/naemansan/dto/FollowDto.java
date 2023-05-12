package com.dongguk.cse.naemansan.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FollowDto {
    private Long userId;
    private String userName;

    @Builder
    public FollowDto(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
