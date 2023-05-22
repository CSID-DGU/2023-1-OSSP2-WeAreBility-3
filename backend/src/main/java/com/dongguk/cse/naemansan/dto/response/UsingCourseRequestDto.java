package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.dto.PointDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsingCourseRequestDto {
    private Long enrollment_id;
    private List<PointDto> locations;
}
