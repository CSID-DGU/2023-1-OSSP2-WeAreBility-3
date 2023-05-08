package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import jakarta.persistence.*;

import java.util.ArrayList;
import org.springframework.data.geo.Point;

import java.util.Date;
import java.util.List;
@Entity
@Table(name="courses")
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id")
    private int user_id;
    @Column(name="title",unique = true)
    private String title;
    @Column(name="created_date")
    private Date created_date;
    @Column(name="introduction")
    private String introduction;
    @Column(name="start_location")
    private String start_location;
    @ElementCollection
    @Column(name="locations")
    private List<Point> locations= new ArrayList<>();
    @Column(name="start_point")
    private Point startpoint;
    @Column(name="distance")
    private int distance;
    @Column(name="status")
    private int status;

    public Point getStartpoint() {
        return startpoint;
    }

    public Long getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public String getIntroduction() {
        return introduction;
    }

    public int getDistance() {
        return distance;
    }
    public int getStatus() {
        return status;
    }
    public String getStart_location() {
        return start_location;
    }

    public List<Point> getLocations() {
        return locations;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setLocations(List<Point> locations) {
        this.locations = locations;
    }

    public void setStartpoint(Point startpoint) {
        this.startpoint = startpoint;
    }
}
