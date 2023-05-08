package com.dongguk.cse.naemansan.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String name;
    private String imagePath;
    private String information;
}
