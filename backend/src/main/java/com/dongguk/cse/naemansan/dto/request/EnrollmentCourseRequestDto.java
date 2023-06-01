package com.dongguk.cse.naemansan.dto.request;

import com.dongguk.cse.naemansan.dto.CourseTagDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCourseRequestDto {
    private Long individual_id;
    private String title;
    private String introduction;
    private List<CourseTagDto> tags;
}
