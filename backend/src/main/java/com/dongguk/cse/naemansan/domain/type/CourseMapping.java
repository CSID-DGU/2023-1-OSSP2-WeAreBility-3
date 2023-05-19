package com.dongguk.cse.naemansan.domain.type;

import java.sql.Timestamp;

public interface CourseMapping {
    Long getId();
    String getTitle();
    Timestamp getCreatedDate();
    String getStartLocationName();
    Double getDistance();
    Double getRadius();
}