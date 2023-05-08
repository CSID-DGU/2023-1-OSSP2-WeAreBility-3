package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import jakarta.persistence.*;

@Entity
@Table(name="course_types")
public class CourseType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="course_id")
    private int courseId;
    @Transient
    private CourseTagType courseTagType;
    @Column(name="tag")
    private String courseTag;

    public int getCourseId() {
        return courseId;
    }

    public Long getId() {
        return id;
    }

    public CourseTagType getCourseTagType() {
        return courseTagType;
    }

    public String getCourseTag() {
        return courseTag;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setCourseTagType(CourseTagType courseTagType) {
        this.courseTagType = courseTagType;
    }

    public void setCourseTag(String courseTag) {
        this.courseTag = courseTag;
    }
}
