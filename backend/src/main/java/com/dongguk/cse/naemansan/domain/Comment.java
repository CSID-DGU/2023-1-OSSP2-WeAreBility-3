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

    @JoinColumn(name = "user_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "course_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private EnrollmentCourse enrollmentCourse;

    @Column(name = "create_date", nullable = false)
    private Timestamp createdDate;

    @Column(name = "context", nullable = false)
    private String content;

    @Column(name = "is_edit", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isEdit;

    @Column(name = "status", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean status;

    @Builder
    public Comment(User user, EnrollmentCourse enrollmentCourse, String content) {
        this.user = user;
        this.enrollmentCourse = enrollmentCourse;
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
        this.content = content;
        this.isEdit = false;
        this.status = true;
    }
}
