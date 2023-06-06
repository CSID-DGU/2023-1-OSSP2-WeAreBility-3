package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;
@Getter
public class EnrollmentCourseDetailDto {
    private Long id;
    private Long user_id;
    private String user_name;
    private String title;
    private Timestamp created_date;
    private String introduction;
    private List<CourseTagDto> tags;
    private String start_location_name;
    private List<PointDto> locations;
    private Double distance;
    private Long like_cnt;
    private Boolean is_like;

    @Builder
    public EnrollmentCourseDetailDto(Long id, Long user_id, String user_name, String title, Timestamp created_date, String introduction,
                                     List<CourseTagDto> tags, String start_location_name, List<PointDto> locations, Double distance,
                                     Long like_cnt, Boolean is_like) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.title = title;
        this.created_date = created_date;
        this.introduction = introduction;
        this.tags = tags;
        this.start_location_name = start_location_name;
        this.locations = locations;
        this.distance = distance;
        this.like_cnt = like_cnt;
        this.is_like = is_like;
    }
}
