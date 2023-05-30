package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.domain.Image;
import com.dongguk.cse.naemansan.dto.PointDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShopDto {
    private Long id;
    private String name;
    private String introduction;
    private String image_path;
    private PointDto location;

    @Builder
    public ShopDto(Long id, String name, String introduction, Image image, PointDto location) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.image_path = image.getPath();
        this.location = location;
    }
}
