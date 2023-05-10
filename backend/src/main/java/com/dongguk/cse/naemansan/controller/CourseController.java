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
    public ResponseDto<CourseDto> createCourse(Authentication authentication, @RequestBody CourseRequestDto courseRequestDto){
        return new ResponseDto<CourseDto>(courseService.createCourse(Long.valueOf(authentication.getName()), courseRequestDto));
    }

    // Course Read
    @GetMapping("/course/{courseId}")
    public ResponseDto<CourseDto> readCourse(@PathVariable Long courseId) {
        return new ResponseDto<CourseDto>(courseService.readCourse(Long.valueOf(courseId)));
    }

    // Course Update
    @PutMapping("/course/{courseId}")
    public ResponseDto<CourseDto> updateCourse(Authentication authentication, @PathVariable Long courseId, @RequestBody CourseRequestDto courseRequestDto) {
        return new ResponseDto<CourseDto>(courseService.updateCourse(Long.valueOf(authentication.getName()), courseId, courseRequestDto));
    }

    // Course Delete
    @DeleteMapping("/course/{courseId}")
    public ResponseDto<Boolean> deleteCourse(Authentication authentication, @PathVariable Long courseId) {
        return new ResponseDto<Boolean>(courseService.deleteCourse(Long.valueOf(authentication.getName()), Long.valueOf(courseId)));
    }
}
