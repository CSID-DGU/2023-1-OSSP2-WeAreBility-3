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
    @Column(name="course_id")
    private int courseId;
    @Column(name="tag")
    private String courseTag;

    @Builder
    public CourseTag(Long id, int courseId, String courseTag) {
        this.id = id;
        this.courseId = courseId;
        this.courseTag = courseTag;
    }
}
