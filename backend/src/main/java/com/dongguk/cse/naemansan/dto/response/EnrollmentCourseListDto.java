package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.dto.CourseTagDto;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;
@Getter
public class EnrollmentCourseListDto {
    private Long id;
    private String title;
    private Timestamp createdDateTime;
    private List<CourseTagDto> courseTags;
    private String startLocationName;
    private Double distance;
    private Long likeCnt;
    private Long usingCnt;
    private Boolean isLike;

    @Builder
    public EnrollmentCourseListDto(Long id, String title, Timestamp createdDateTime,
                                   List<CourseTagDto> courseTags, String startLocationName,
                                   Double distance, Long likeCnt, Long usingCnt, Boolean isLike) {
        this.id = id;
        this.title = title;
        this.createdDateTime = createdDateTime;
        this.courseTags = courseTags;
        this.startLocationName = startLocationName;
        this.distance = distance;
        this.likeCnt = likeCnt;
        this.usingCnt = usingCnt;
        this.isLike = isLike;
    }
}
