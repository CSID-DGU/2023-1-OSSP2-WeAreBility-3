package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.domain.CourseType;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import com.dongguk.cse.naemansan.repository.CourseRepository;
import com.dongguk.cse.naemansan.repository.CourseTypeRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseTypeRepository courseTypeRepository;

    public CourseService(CourseRepository courseRepository, CourseTypeRepository courseTypeRepository) {
        this.courseRepository = courseRepository;
        this.courseTypeRepository = courseTypeRepository;
    }

    //산책도 등록
    public Long join(Course course, CourseType courseType) {
        validateDuplicateCourse(course);//중복 검사
        if (!validateCourseTag(courseType))
            throw new IllegalStateException("적절하지 않은 태그 입니다");
        courseRepository.save(course);
        courseType.setCourseId(Math.toIntExact(course.getId()));
        courseTypeRepository.save(courseType);
        return course.getId();
    }

    //산책로 중복 검사 (이름으로 바꿔야 할 수도?)
    private void validateDuplicateCourse(Course course) {
        courseRepository.findByTitle(course.getTitle())
                .ifPresent(w -> {
                    throw new IllegalStateException("이미 존재하는 산책로 입니다");
                });
    }

    //산책로 태그 확인
    private boolean validateCourseTag(CourseType courseType) {
        //CourseTagType courseTagType = courseType.getCourseTagType();
        //태그 맞는디 equal로 확인
        for (CourseTagType courseTagType : CourseTagType.values()) {
            if (courseTagType.name().equals(courseType.getCourseTag())) {
                courseType.setCourseTag(courseType.getCourseTag());
                return true;
            }
        }
        return false;
    }

    //전체 산책로 조회
    public List<Course> findCourses() {
        return courseRepository.findAll();
    }

    //산책로 하나 찾기
    public Optional<Course> findOne(Long courseId) {
        return courseRepository.findById(courseId);
    }

    //제목으로 산책로 찾기
    public Optional<Course> findOneTitle(String courseTitle) {
        return courseRepository.findByTitle(courseTitle);
    }

    //키워드 기반 조회
    public Optional<Course> OrderbyKeword(String tag) {
        return courseRepository.orderByKeyword(tag);
    }

    //산책로 삭제
    public void delete(Course course) {
        courseRepository.deleteCourse(course.getId());
    }

    //산책로 수정
    public Long update(Course course) {
        return courseRepository.updateCourse(course.getId(), course.getTitle(), course.getCreated_date(), course.getIntroduction(), course.getStart_location(), course.getStatus());
    }

}
