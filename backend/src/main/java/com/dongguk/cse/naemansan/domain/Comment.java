package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
@DynamicUpdate
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
    @Column(name = "context", nullable = false)
    private String context;
    @Column(name = "is_edit", columnDefinition = "TINYINT(1)")
    private Boolean isEdit;
    @Column(name = "status", columnDefinition = "TINYINT(1)")
    private Boolean status;

    @Builder
    public Comment(Long userId, Long courseId, String context) {
        this.userId = userId;
        this.courseId = courseId;
        this.context = context;
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
        this.isEdit = false;
        this.status = true;
    }
}
