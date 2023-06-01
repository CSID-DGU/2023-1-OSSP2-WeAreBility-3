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
    private Timestamp created_date;
    private List<CourseTagDto> tags;
    private String start_location_name;
    private Double distance;
    private Long like_cnt;
    private Long using_unt;
    private Boolean is_like;

    @Builder
    public EnrollmentCourseListDto(Long id, String title, Timestamp created_date,
                                   List<CourseTagDto> tags, String start_location_name,
                                   Double distance, Long like_cnt, Long using_unt, Boolean is_like) {
        this.id = id;
        this.title = title;
        this.created_date = created_date;
        this.tags = tags;
        this.start_location_name = start_location_name;
        this.distance = distance;
        this.like_cnt = like_cnt;
        this.using_unt = using_unt;
        this.is_like = is_like;
    }
}
