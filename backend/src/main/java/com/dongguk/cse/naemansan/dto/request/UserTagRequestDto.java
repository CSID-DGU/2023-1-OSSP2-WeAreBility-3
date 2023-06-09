package com.dongguk.cse.naemansan.dto.request;

import com.dongguk.cse.naemansan.dto.CourseTagDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserTagRequestDto {
    List<CourseTagDto> tags;
}