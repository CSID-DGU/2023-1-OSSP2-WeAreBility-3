package com.dongguk.cse.naemansan.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class CommentDto {
    private Long id;
    private Long user_id;
    private Long course_id;
    private String user_name;
    private String content;
    private Timestamp created_date;
    private Boolean is_edit;

    @Builder
    public CommentDto(Long id, Long user_id, Long course_id, String user_name, String content, Timestamp created_date, Boolean is_edit) {
        this.id = id;
        this.user_id = user_id;
        this.course_id = course_id;
        this.user_name = user_name;
        this.content = content;
        this.created_date = created_date;
        this.is_edit = is_edit;
    }
}
