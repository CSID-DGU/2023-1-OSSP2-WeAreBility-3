package com.dongguk.cse.naemansan.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class CommentDto {
    private Long id;
    private Long userId;
//    private String username;
    private String content;
    private Timestamp createdDateTime;
    private Boolean isEdit;

//    @Builder
//    public CommentDto(Long id, Long userId, String username, String content, Timestamp createdDateTime, Boolean isEdit) {
//        this.id = id;
//        this.userId = userId;
//        this.username = username;
//        this.content = content;
//        this.createdDateTime = createdDateTime;
//        this.isEdit = isEdit;
//    }
    
    // 수정 예정 - UserName 받을 수 있도록 Join 연산 사용 예정
    @Builder
    public CommentDto(Long id, Long userId, String content, Timestamp createdDateTime, Boolean isEdit) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.isEdit = isEdit;
}
}
