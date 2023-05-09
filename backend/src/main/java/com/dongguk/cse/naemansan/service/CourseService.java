package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.dto.CourseDto;
import com.dongguk.cse.naemansan.dto.CourseRequestDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import com.dongguk.cse.naemansan.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.DoubleBuffer;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();
//    //산책도 등록
//    public Long join(Course course, CourseTag courseTag) {
//        validateDuplicateCourse(course);//중복 검사
//        if (!validateCourseTag(courseTag))
//            throw new IllegalStateException("적절하지 않은 태그 입니다");
//        courseRepository.save(course);
//        courseTag.setCourseId(Math.toIntExact(course.getId()));
//        courseTypeRepository.save(courseTag);
//        return course.getId();
//    }
//
//    //산책로 중복 검사 (이름으로 바꿔야 할 수도?)
//    private void validateDuplicateCourse(Course course) {
//        courseRepository.findByTitle(course.getTitle())
//                .ifPresent(w -> {
//                    throw new IllegalStateException("이미 존재하는 산책로 입니다");
//                });
//    }

    //산책로 태그 확인
//    private boolean validateCourseTag(CourseTag courseTag) {
//        //CourseTagType courseTagType = courseType.getCourseTagType();
//        //태그 맞는디 equal로 확인
//        for (CourseTagType courseTagType : CourseTagType.values()) {
//            if (courseTagType.name().equals(courseTag.getCourseTag())) {
//                courseTag.setCourseTag(courseTag.getCourseTag());
//                return true;
//            }
//        }
//        return false;
//    }
//
//    //전체 산책로 조회
//    public List<Course> findCourses() {
//        return courseRepository.findAll();
//    }
//
//    //산책로 하나 찾기
//    public Optional<Course> findOne(Long courseId) {
//        return courseRepository.findById(courseId);
//    }
//
//    //제목으로 산책로 찾기
//    public Optional<Course> findOneTitle(String courseTitle) {
//        return courseRepository.findByTitle(courseTitle);
//    }
//
//    //키워드 기반 조회
//    public Optional<Course> OrderbyKeword(String tag) {
//        return courseRepository.orderByKeyword(tag);
//    }



    // Course Create
    public Boolean createCourse(Long userId, CourseRequestDto courseRequestDto) {
        Optional<Course> course = courseRepository.findByTitle(courseRequestDto.getTitle());

        if (!course.isEmpty()) {
            log.error("course Name Duplication - user : {}, {}", userId, courseRequestDto);
            return Boolean.FALSE;
        }

        if (courseRequestDto.getPointDtos().size() == 0) {
            log.error("Not Exist Points - user : {}, {}", userId, courseRequestDto);
            return Boolean.FALSE;
        }


        Point points[] = new Point[courseRequestDto.getPointDtos().size()];

        PointDto pointDtoOne = null;
        PointDto pointDtoTwo = null;
        double distance = 0.0;
        for (int i = 0; i < courseRequestDto.getPointDtos().size(); i++) {
            PointDto pointDto = courseRequestDto.getPointDtos().get(i);
            pointDtoOne = pointDtoTwo;
            pointDtoTwo = pointDto;
            distance += getPointDistance(pointDtoOne, pointDtoTwo);
            points[i] = geometryFactory.createPoint(new Coordinate(pointDto.getLatitude(), pointDto.getLongitude()));
        }

        MultiPoint multiPoint = geometryFactory.createMultiPoint(points);

        courseRepository.save(Course.builder()
                .userId(userId)
                .title(courseRequestDto.getTitle())
                .introduction(courseRequestDto.getIntroduction())
                .startLocationName("임시 시작 위치")
                .startLocation(points[0])
                .locations(multiPoint)
                .distance(distance)
                .status(true).build());

        return Boolean.TRUE;
    }

    // Course Read
    public CourseDto readCourse(Long courseId) {
        return null;
    }

    public Boolean updateCourse(CourseRequestDto courseRequestDto) {
        return Boolean.FALSE;
    }

    public Boolean deleteCourse(Long courseId) {
        return Boolean.FALSE;
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
}
