package com.dongguk.cse.naemansan.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FollowDto {
    private Long user_id;
    private String user_name;

    @Builder
    public FollowDto(Long user_id, String user_name) {
        this.user_id = user_id;
        this.user_name = user_name;
    }
}
