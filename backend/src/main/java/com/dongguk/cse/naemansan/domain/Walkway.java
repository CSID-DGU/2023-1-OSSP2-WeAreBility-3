package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import org.springframework.data.geo.Point;
import java.util.List;
@Entity
public class Walkway {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int user_id;
    private String title;
    private String created_date;
    private String introduction;
    private String start_location;
    @ElementCollection
    private List<Point> locations= new ArrayList<>();
    private int distance;
    private int status;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreated_date() {
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

    public void setId(int id) {
        this.id = id;
    }
    public void setCreated_date(String created_date) {
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
}
