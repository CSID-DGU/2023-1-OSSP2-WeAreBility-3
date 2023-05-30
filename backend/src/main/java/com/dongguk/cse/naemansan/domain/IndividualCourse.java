package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Table(name="individual_courses")
public class IndividualCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name="title",unique = true)
    private String title;

    @Column(name="created_date")
    private Timestamp createdDate;

    @Column(name="locations", columnDefinition = "MULTIPOINT")
    private MultiPoint locations;

    @Column(name="distance")
    private Double distance;

    @Column(name = "status", columnDefinition = "TINYINT(1)")
    private Boolean status;

    @Builder
    public IndividualCourse(User user, String title, MultiPoint locations, Double distance) {
        this.user = user;
        this.title = title;
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
        this.locations = locations;
        this.distance = distance;
        this.status = true;
    }
}
