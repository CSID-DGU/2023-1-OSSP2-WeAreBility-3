package com.dongguk.cse.naemansan.dto.request;

import com.dongguk.cse.naemansan.dto.PointDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IndividualCourseRequestDto {
    private String title;
    private List<PointDto> locations;
}
