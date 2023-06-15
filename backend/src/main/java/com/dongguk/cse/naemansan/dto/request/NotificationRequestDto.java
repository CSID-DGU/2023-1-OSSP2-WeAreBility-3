package com.dongguk.cse.naemansan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {
    private String targetToken; //추가
    private String title; //추가
    private String body; //수정
}
