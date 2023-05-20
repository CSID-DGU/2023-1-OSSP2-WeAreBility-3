package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;
@Getter
public class CourseDetailDto {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private Timestamp createdDateTime;
    private String introduction;
    private List<CourseTagDto> tags;
    private String startLocationName;
    private List<PointDto> locations;

    @Builder
    public CourseDetailDto(Long id, Long userId, String userName, String title, Timestamp createdDateTime, String introduction,
                           List<CourseTagDto> tags, String startLocationName, List<PointDto> locations) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.createdDateTime = createdDateTime;
        this.introduction = introduction;
        this.tags = tags;
        this.startLocationName = startLocationName;
        this.locations = locations;
    }
}
