package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="using_courses")
public class UsingCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "course_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private EnrollmentCourse enrollmentCourse;

    @Column(name = "finish_status", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean finishStatus;

    @Column(name = "using_date", nullable = false)
    private Timestamp usingDate;

    @Builder
    public UsingCourse(User user, EnrollmentCourse enrollmentCourse, Boolean finishStatus) {
        this.user = user;
        this.enrollmentCourse = enrollmentCourse;
        this.finishStatus = finishStatus;
        this.usingDate = Timestamp.valueOf(LocalDateTime.now());
    }
}
