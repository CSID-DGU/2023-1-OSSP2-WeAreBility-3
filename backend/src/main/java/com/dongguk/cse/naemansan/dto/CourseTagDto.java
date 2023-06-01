package com.dongguk.cse.naemansan.dto;

import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import com.dongguk.cse.naemansan.domain.type.TagStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseTagDto {
    private CourseTagType name;
    private TagStatusType status;
}
