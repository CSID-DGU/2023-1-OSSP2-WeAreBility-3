package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    //산책로 맵핑
    @GetMapping("/courses/new")
    public String createForm(){
        return "courses/creatWalkwayForm";
    }
    //산책로 생성
    @PostMapping("/courses/new")
    public String create(CourseForm form){
        Course course = new Course();
        course.setId(form.getId());
        course.setUser_id(form.getUser_id());
        course.setTitle(form.getTitle());
        course.setCreated_date(form.getCreated_date());
        course.setIntroduction(form.getIntroduction());
        course.setStart_location(form.getStart_location());
        course.setLocations(form.getLocations());
        course.setDistance(form.getDistance());
        course.setStatus(form.getStatus());
        //courseService.join(course);

        return "redirect:/";
    }

    @GetMapping("/courses")
    public String list(Model model){
        List<Course> courses = courseService.findCourses();
        model.addAttribute("courses", courses);
        return "courses/courseList";
    }

}
