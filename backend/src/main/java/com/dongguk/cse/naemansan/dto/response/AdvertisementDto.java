package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.domain.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdvertisementDto {
    private Long id;
    private String name;
    private String url;
    private String image_path;

    @Builder
    public AdvertisementDto(Long id, String name, String url, Image image) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.image_path = image.getUuidName();
    }
}
