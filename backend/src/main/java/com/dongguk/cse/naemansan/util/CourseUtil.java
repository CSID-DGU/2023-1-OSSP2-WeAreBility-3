package com.dongguk.cse.naemansan.util;

import com.dongguk.cse.naemansan.domain.EnrollmentCourse;
import com.dongguk.cse.naemansan.domain.CourseTag;
import com.dongguk.cse.naemansan.domain.Like;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.domain.type.TagStatusType;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseUtil {
    @Value("${client.geocoding.api-url: aaa.bbb.ccc}")
    private String GOOGLE_MAP_URL;
    @Value("${client.geocoding.api-key: aaa.bbb.ccc}")
    private String GOOGLE_MAP_API_KEY;

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    private String getStringPoint(PointDto pointDto) {
        return pointDto.getLatitude().toString()+","+ pointDto.getLongitude().toString();
    }

    public String getLocationName(PointDto pointDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(GOOGLE_MAP_URL);
        sb.append("?latlng=" + getStringPoint(pointDto));
        sb.append("&key=" + GOOGLE_MAP_API_KEY);
        sb.append("&language=" + "ko");

        ResponseEntity<String> response = restTemplate.exchange(
                sb.toString(),
                HttpMethod.GET,
                null,
                String.class
        );

        JsonArray jsonArray = JsonParser.parseString(response.getBody()).getAsJsonObject().getAsJsonArray("results");

        String locationName = null;
        for (Object object : jsonArray) {
            JsonObject jsonObject = (JsonObject) object;
            String tempLocation = jsonObject.get("formatted_address").getAsString();

            if (tempLocation.startsWith("대한민국 ")) {
                locationName =  tempLocation.substring("대한민국 ".length(), tempLocation.length());
                break;
            }
        }

        return locationName;
    }

    public double getPointDistance(PointDto pointDtoOne, PointDto pointDtoTwo) {
        if (pointDtoOne == null || pointDtoTwo == null) {
            return 0.0;
        }

        double theta = pointDtoOne.getLongitude() - pointDtoTwo.getLongitude();
        double distance = Math.sin(deg2rad(pointDtoOne.getLatitude())) * Math.sin(deg2rad(pointDtoTwo.getLatitude()))
                + Math.cos(deg2rad(pointDtoOne.getLatitude())) * Math.cos(deg2rad(pointDtoTwo.getLatitude()))
                * Math.cos(deg2rad(theta));

        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = distance * 60 * 1.1515 * 1609.344;
        return distance;
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public Point getLatLng2Point(Double latitude, Double longitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    public Map<String, Object> getPointDto2Point(List<PointDto> inputPoints) {
        Map<String, Object> map = new HashMap<>();

        // MultiPoint 만드는 과정
        Point points[] = new Point[inputPoints.size()];

        PointDto pointDtoOne = null;
        PointDto pointDtoTwo = null;
        double distance = 0.0;
        for (int i = 0; i < inputPoints.size(); i++) {
            PointDto pointDto = inputPoints.get(i);
            pointDtoOne = pointDtoTwo;
            pointDtoTwo = pointDto;
            distance += getPointDistance(pointDtoOne, pointDtoTwo);
            points[i] = geometryFactory.createPoint(new Coordinate(pointDto.getLongitude(), pointDto.getLatitude()));
        }

        MultiPoint multiPoint = geometryFactory.createMultiPoint(points);

        map.put("startLocation", points[0]);
        map.put("locations", multiPoint);
        map.put("distance", distance);

        return map;
    }

    public List<PointDto> getPoint2PointDto(MultiPoint multiPoint) {
        List<PointDto> locations = new ArrayList<>();

        for (int i = 0; i < multiPoint.getNumGeometries(); i++) {
            locations.add(new PointDto(multiPoint.getGeometryN(i).getCoordinate().getY(),
                    multiPoint.getGeometryN(i).getCoordinate().getX()));
        }

        return locations;
    }

    public List<CourseTag> getTagDto2Tag(EnrollmentCourse enrollmentCourse, List<CourseTagDto> dtoList) {
        List<CourseTag> tagList = new ArrayList<>();

        for (CourseTagDto courseTagDto : dtoList) {
            tagList.add(CourseTag.builder()
                    .enrollmentCourse(enrollmentCourse).courseTagType(courseTagDto.getCourseTagType()).build());
        }

        return tagList;
    }

    public List<CourseTagDto> getTag2TagDto(List<CourseTag> tagList) {
        List<CourseTagDto> dtoList = new ArrayList<>();

        for (CourseTag courseTag : tagList) {
            dtoList.add(CourseTagDto.builder()
                    .courseTagType(courseTag.getCourseTagType())
                    .tagStatusType(TagStatusType.DEFAULT).build());
        }

        return dtoList;
    }

    public boolean existLike(User user, EnrollmentCourse enrollmentCourse) {
        for (Like like : user.getLikes()) {
            if (!like.getEnrollmentCourse().equals(enrollmentCourse)) {
                continue;
            }
            return true;
        }
        return false;
    }
}
