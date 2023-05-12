package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.response.CourseDto;
import com.dongguk.cse.naemansan.dto.request.CourseRequestDto;
import com.dongguk.cse.naemansan.dto.ResponseDto;
import com.dongguk.cse.naemansan.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/course/location/{latitude}/{longitude}")
    public ResponseDto<List<CourseDto>> getCourseListByLocations(@PathVariable Double latitude, @PathVariable Double longitude) {
        return new ResponseDto<List<CourseDto>>(courseService.getCourseListByLocation(latitude, longitude));
    }

    @GetMapping("/course/tag/{tag}")
    public ResponseDto<List<CourseDto>> getCourseListByTag(@PathVariable String tag) {
        return new ResponseDto<List<CourseDto>>(courseService.getCourseListByTag(tag));
    }

    @GetMapping("/course/like/{courseId}")
    public ResponseDto<Boolean> likeCourse(Authentication authentication, @PathVariable Long courseId) {
        return new ResponseDto<Boolean>(courseService.likeCourse(Long.valueOf(authentication.getName()), courseId));
    }

    @DeleteMapping("/course/like/{courseId}")
    public ResponseDto<Boolean> dislikeCourse(Authentication authentication, @PathVariable Long courseId) {
        return new ResponseDto<Boolean>(courseService.dislikeCourse(Long.valueOf(authentication.getName()), courseId));
    }
}
