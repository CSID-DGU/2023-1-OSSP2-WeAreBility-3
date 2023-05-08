package com.dongguk.cse.naemansan.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public class UserRequestDto {
    private String name;
    private String imagePath;
    private String information;

    @Builder
    public UserRequestDto(String name, String imagePath, String information) {
        this.name = name;
        this.imagePath = imagePath;
        this.information = information;
    }
}
