package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;
@Getter
public class CourseListDto {
    private Long id;
    private String title;
    private Timestamp createdDateTime;
    private List<CourseTagDto> courseTags;
    private String startLocationName;
    private Double distance;

    @Builder
    public CourseListDto(Long id, String title, Timestamp createdDateTime,
                         List<CourseTagDto> courseTags, String startLocationName, Double distance) {
        this.id = id;
        this.title = title;
        this.createdDateTime = createdDateTime;
        this.courseTags = courseTags;
        this.startLocationName = startLocationName;
        this.distance = distance;
    }
}
