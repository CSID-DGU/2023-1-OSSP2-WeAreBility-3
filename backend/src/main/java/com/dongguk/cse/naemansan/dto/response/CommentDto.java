package com.dongguk.cse.naemansan.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class CommentDto {
    private Long id;
    private Long userId;
    private Long courseId;
    private String userName;
    private String content;
    private Timestamp createdDateTime;
    private Boolean isEdit;

    @Builder
    public CommentDto(Long id, Long userId, Long courseId, String userName, String content, Timestamp createdDateTime, Boolean isEdit) {
        this.id = id;
        this.userId = userId;
        this.courseId =courseId;
        this.userName = userName;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.isEdit = isEdit;
    }
}
