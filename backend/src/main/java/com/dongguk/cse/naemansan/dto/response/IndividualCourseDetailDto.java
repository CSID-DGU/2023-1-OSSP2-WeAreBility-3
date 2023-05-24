package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.dto.PointDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndividualCourseDetailDto {
    private Long id;
    private String title;
    private List<PointDto> locations;
    private Timestamp create_date;
    private Double distance;
}
