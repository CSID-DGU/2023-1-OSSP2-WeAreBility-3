package com.dongguk.cse.naemansan.dto.request;

import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDto {
    private String title;
    private String introduction;
    private List<CourseTagDto> courseTags;
    private List<PointDto> pointDtos;
}
