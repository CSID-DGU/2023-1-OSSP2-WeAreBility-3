package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.CourseDto;
import com.dongguk.cse.naemansan.dto.CourseRequestDto;
import com.dongguk.cse.naemansan.dto.ResponseDto;
import com.dongguk.cse.naemansan.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    // Course Create
    @PostMapping("/course")
    public ResponseDto<Boolean> createCourse(Authentication authentication, @RequestBody CourseRequestDto courseRequestDto){
        return new ResponseDto<Boolean>(courseService.createCourse(Long.valueOf(authentication.getName()), courseRequestDto));
    }

    //Course Read
    @GetMapping("/course")
    public ResponseDto<CourseDto> readCourse(@RequestParam("courseId") String courseId) {
        return new ResponseDto<CourseDto>(courseService.readCourse(Long.valueOf(courseId)));
    }

    //Course Update
    @PutMapping("/course/{courseId}")
    public ResponseDto<Boolean> updateCourse(Authentication authentication, @PathVariable Long courseId, @RequestBody CourseRequestDto courseRequestDto) {
        return new ResponseDto<Boolean>(courseService.updateCourse(Long.valueOf(authentication.getName()), courseId, courseRequestDto));
    }

    //Course Delete
    @DeleteMapping("/course")
    public ResponseDto<Boolean> deleteCourse(@RequestParam("courseId") String courseId) {
        return new ResponseDto<Boolean>(courseService.deleteCourse(Long.valueOf(courseId)));
    }
    //산책로 생성
//    @PostMapping("/course")
//    public String create(CourseForm form){
//        Course course = new Course();
//        course.setId(form.getId());
//        course.setUser_id(form.getUser_id());
//        course.setTitle(form.getTitle());
//        course.setCreated_date(form.getCreated_date());
//        course.setIntroduction(form.getIntroduction());
//        course.setStart_location(form.getStart_location());
//        course.setLocations(form.getLocations());
//        course.setDistance(form.getDistance());
//        course.setStatus(form.getStatus());
//        //courseService.join(course);
//
//        return "redirect:/";
//    }
//
//    @GetMapping("/courses")
//    public String list(Model model){
//        List<Course> courses = courseService.findCourses();
//        model.addAttribute("courses", courses);
//        return "courses/courseList";
//    }

}
