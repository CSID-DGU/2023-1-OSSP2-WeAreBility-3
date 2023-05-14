package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.MultiPoint;

import java.sql.Timestamp;
import java.util.List;
@Getter
public class CourseDto {
    private Long id;
    private Long userId;
    private String title;
    private Timestamp createdDateTime;
    private String introduction;
    private List<CourseTagDto> courseTags;
    private String startLocationName;
    private List<PointDto> locations;

    @Builder
    public CourseDto(Long id, Long userId, String title, Timestamp createdDateTime, String introduction,
                     List<CourseTagDto> courseTags, String startLocationName, List<PointDto> locations) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.createdDateTime = createdDateTime;
        this.introduction = introduction;
        this.courseTags = courseTags;
        this.startLocationName = startLocationName;
        this.locations = locations;
    }
}
