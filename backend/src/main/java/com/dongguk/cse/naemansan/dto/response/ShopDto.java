package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.dto.PointDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopDto {
    private Long id;
    private String name;
    private String introduction;
    private PointDto location;
}
