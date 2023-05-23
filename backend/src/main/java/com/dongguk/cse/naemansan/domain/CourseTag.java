package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="course_tags")
public class CourseTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "course_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private EnrollmentCourse enrollmentCourse;

    @Column(name = "tag")
    @Enumerated(EnumType.STRING)
    private CourseTagType courseTagType;

    @Builder
    public CourseTag(EnrollmentCourse enrollmentCourse, CourseTagType courseTagType) {
        this.enrollmentCourse = enrollmentCourse;
        this.courseTagType = courseTagType;
    }
}
