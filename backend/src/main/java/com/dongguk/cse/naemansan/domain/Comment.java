package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "course_id")
    private Long courseId;
    @Column(name = "create_date")
    private Timestamp createdDate;
    @Column(name = "isEdit", columnDefinition = "TINYINT(1)")
    private boolean isEdit;
    @Column(name = "status", columnDefinition = "TINYINT(1)")
    private boolean status;

    @Builder
    public Comment(Long id, Long userId, Long courseId, Timestamp createdDate, boolean isEdit, boolean status) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.createdDate = createdDate;
        this.isEdit = isEdit;
        this.status = status;
    }
}
