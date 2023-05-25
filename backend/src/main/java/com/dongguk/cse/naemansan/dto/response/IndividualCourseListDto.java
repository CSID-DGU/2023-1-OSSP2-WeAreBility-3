package com.dongguk.cse.naemansan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndividualCourseListDto {
    private Long id;
    private String title;
    private Timestamp created_date;
    private Double distance;
}
