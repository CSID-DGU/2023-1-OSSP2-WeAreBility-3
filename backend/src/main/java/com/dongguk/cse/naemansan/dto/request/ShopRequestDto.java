package com.dongguk.cse.naemansan.dto.request;

import com.dongguk.cse.naemansan.dto.PointDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShopRequestDto {
    private String name;
    private String introduction;
    private PointDto location;
}
