package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.repository.CourseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Transactional
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    //산책도 등록
    public Long join(Course course){
        validateDuplicateWalkway(course);//중복 검사
        courseRepository.save(course);
        return course.getId();
    }
    //산책로 중복 검사 (이름으로 바꿔야 할 수도?)
    private void validateDuplicateWalkway(Course course){
            courseRepository.findByTitle(course.getTitle())
                    .ifPresent(w -> {
                        throw  new IllegalStateException("이미 존재하는 산책로 입니다");
                    });
    }

    //전체 산책로 조회
    public List<Course> findCourses(){
        return courseRepository.findAll();
    }
    //산책로 하나 찾기
    public Optional<Course> findOne(Long courseId){
        return courseRepository.findById(courseId);
    }
    //제목으로 산책로 찾기
    public Optional<Course> findOneTitle(String courseTitle){
        return courseRepository.findByTitle(courseTitle);
    }
    //키워드 기반 조회
    public Optional<Course> OrderbyKeword(String tag) {
        return courseRepository.orderByKeyword(tag);
    }
    //산책로 삭제
    public void delete(Course course){
        courseRepository.deleteCourse(course.getId());
    }
    //산책로 수정
    public Long update(Course course){
        return courseRepository.updateCourse(course.getId(),course.getTitle(),course.getCreated_date(),course.getIntroduction(),course.getStart_location(),course.getStatus());
    }

}
