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
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Table(name="courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="user_id")
    private Long userId;
    @Column(name="title",unique = true)
    private String title;
    @Column(name="created_date")
    private Timestamp createdDate;
    @Column(name="introduction")
    private String introduction;
    @Column(name="start_location_name")
    private String startLocationName;
    @Column(name="start_location", columnDefinition = "POINT")
    private Point startLocation;
    @Column(name="locations", columnDefinition = "MULTIPOINT")
    private MultiPoint locations;
    @Column(name="distance")
    private double distance;
    @Column(name = "status", columnDefinition = "TINYINT(1)")
    private boolean status;

    @Builder
    public Course(Long userId, String title, String introduction,
                  String startLocationName, Point startLocation, MultiPoint locations, double distance, boolean status) {
        this.userId = userId;
        this.title = title;
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
        this.introduction = introduction;
        this.startLocationName = startLocationName;
        this.startLocation = startLocation;
        this.locations = locations;
        this.distance = distance;
        this.status = status;
    }
}
