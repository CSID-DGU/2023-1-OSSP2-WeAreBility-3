package com.dongguk.cse.naemansan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointDto {
    private Double latitude;
    private Double longitude;

    public String toString() {
        return latitude.toString()+","+ longitude.toString();
    }
}
