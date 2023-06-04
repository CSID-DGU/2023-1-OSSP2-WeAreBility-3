package com.dongguk.cse.naemansan.util;

import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.domain.type.TagStatusType;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import com.dongguk.cse.naemansan.dto.response.EnrollmentCourseListDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
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

    @Value("${client.ml.checker-url: aaa.bbb.ccc}")
    private String ML_CHECKER_URL;
    @Value("${client.ml.finisher-url: aaa.bbb.ccc}")
    private String ML_FINISHER_URL;
    @Value("${client.ml.recommender-url: aaa.bbb.ccc}")
    private String ML_RECOMMENDER_URL;

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public String getLocationName(PointDto point) {
        StringBuilder sb = new StringBuilder();
        sb.append(GOOGLE_MAP_URL);
        sb.append("?latlng=" + point.toString());
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

    public Boolean checkCourse(List<PointDto> locations) {
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();;
        body.put("pointDtos", locations);

        HttpEntity<?> request = new HttpEntity<String>(body.toJSONString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(
                ML_CHECKER_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        return JsonParser.parseString(response.getBody()).getAsJsonObject().get("success").getAsBoolean();
    }

    public Boolean checkFinishState(Long courseId, List<PointDto> locations) {
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();;
        body.put("courseid", courseId);
        body.put("pointDtos", locations);

        HttpEntity<?> request = new HttpEntity<String>(body.toJSONString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(
                ML_FINISHER_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        return JsonParser.parseString(response.getBody()).getAsJsonObject().get("success").getAsBoolean();
    }

    public List<Long> getRecommendList(Long userId) {
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();;
        body.put("userid", userId);

        HttpEntity<?> request = new HttpEntity<String>(body.toJSONString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                ML_RECOMMENDER_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        JsonArray courseIds = (JsonArray) JsonParser.parseString(response.getBody()).getAsJsonObject().get("courseid");

        List<Long> list = new ArrayList<>();
        for (int i = 0; i < courseIds.size(); i++) {
            list.add(courseIds.get(i).getAsJsonObject().get("id").getAsLong());
        }

        return list;
    }

    private double getPointDistance(PointDto pointDtoOne, PointDto pointDtoTwo) {
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

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
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

    public PointDto getPoint2PointDto(Point point) {
        return new PointDto(point.getCoordinate().getY(),
                point.getCoordinate().getX());
    }

    public Point getPointDto2Point(PointDto point) {
        return  geometryFactory.createPoint(new Coordinate(point.getLongitude(), point.getLatitude()));
    }

    public List<PointDto> getPoint2PointDto(MultiPoint multiPoint) {
        List<PointDto> locations = new ArrayList<>();

        for (int i = 0; i < multiPoint.getNumGeometries(); i++) {
            locations.add(new PointDto(multiPoint.getGeometryN(i).getCoordinate().getY(),
                    multiPoint.getGeometryN(i).getCoordinate().getX()));
        }

        return locations;
    }

    public Point getStartLocation(Coordinate before) {
        return geometryFactory.createPoint(before);
    }

    public List<CourseTag> getTagDto2TagForEnrollmentCourse(EnrollmentCourse enrollmentCourse, List<CourseTagDto> dtoList) {
        List<CourseTag> tagList = new ArrayList<>();

        for (CourseTagDto courseTagDto : dtoList) {
            tagList.add(CourseTag.builder()
                    .enrollmentCourse(enrollmentCourse).courseTagType(courseTagDto.getName()).build());
        }

        return tagList;
    }

    public List<UserTag> getTagDto2TagForUser(User user, List<CourseTagDto> dtoList) {
        List<UserTag> tagList = new ArrayList<>();

        for (CourseTagDto tagDto : dtoList) {
            tagList.add(UserTag.builder()
                    .user(user)
                    .tag(tagDto.getName()).build());
        }

        return tagList;
    }

    public List<CourseTagDto> getTag2TagDtoForCourse(List<CourseTag> tagList) {
        List<CourseTagDto> dtoList = new ArrayList<>();

        for (CourseTag courseTag : tagList) {
            dtoList.add(CourseTagDto.builder()
                    .name(courseTag.getCourseTagType())
                    .status(TagStatusType.DEFAULT).build());
        }

        return dtoList;
    }

    public List<CourseTagDto> getTag2TagDtoForUser(List<UserTag> tagList) {
        List<CourseTagDto> dtoList = new ArrayList<>();

        for (UserTag tag : tagList) {
            dtoList.add(CourseTagDto.builder()
                    .name(tag.getTag())
                    .status(TagStatusType.DEFAULT).build());
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

    public List<EnrollmentCourseListDto> getEnrollmentCourseList(User user, Page<EnrollmentCourse> page) {
        List<EnrollmentCourseListDto> enrollmentCourseListDtoList = new ArrayList<>();
        for (EnrollmentCourse enrollmentCourse : page.getContent()) {
            enrollmentCourseListDtoList.add(EnrollmentCourseListDto.builder()
                    .id(enrollmentCourse.getId())
                    .title(enrollmentCourse.getTitle())
                    .created_date(enrollmentCourse.getCreatedDate())
                    .tags(getTag2TagDtoForCourse(enrollmentCourse.getCourseTags()))
                    .start_location_name(enrollmentCourse.getStartLocationName())
                    .distance(enrollmentCourse.getDistance())
                    .like_cnt((long) enrollmentCourse.getLikes().size())
                    .using_unt((long) enrollmentCourse.getUsingCourses().size())
                    .is_like(existLike(user, enrollmentCourse)).build());
        }
        return enrollmentCourseListDtoList;
    }
}
