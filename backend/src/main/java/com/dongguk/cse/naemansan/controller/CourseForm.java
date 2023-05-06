package com.dongguk.cse.naemansan.controller;

import org.springframework.data.geo.Point;

import java.util.Date;
import java.util.List;

public class CourseForm {
    private Long id;
    private int user_id;
    private String title;
    private Date created_date;
    private String introduction;
    private String start_location;
    private List<Point> locations;
    private int distance;
    private int status;

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

    public String getStart_location() {
        return start_location;
    }

    public List<Point> getLocations() {
        return locations;
    }

    public int getStatus() {
        return status;
    }

    public int getDistance() {
        return distance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public void setLocations(List<Point> locations) {
        this.locations = locations;
    }
}
