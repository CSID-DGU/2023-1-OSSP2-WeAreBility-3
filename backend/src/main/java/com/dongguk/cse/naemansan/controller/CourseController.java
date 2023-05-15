package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.response.CourseDto;
import com.dongguk.cse.naemansan.dto.request.CourseRequestDto;
import com.dongguk.cse.naemansan.dto.ResponseDto;
import com.dongguk.cse.naemansan.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    // Course Create
    @PostMapping("")
    public ResponseDto<CourseDto> createCourse(Authentication authentication, @RequestBody CourseRequestDto courseRequestDto){
        return new ResponseDto<CourseDto>(courseService.createCourse(Long.valueOf(authentication.getName()), courseRequestDto));
    }

    // Course Read
    @GetMapping("/{courseId}")
    public ResponseDto<CourseDto> readCourse(@PathVariable Long courseId) {
        return new ResponseDto<CourseDto>(courseService.readCourse(Long.valueOf(courseId)));
    }

    // Course Update
    @PutMapping("/{courseId}")
    public ResponseDto<CourseDto> updateCourse(Authentication authentication, @PathVariable Long courseId, @RequestBody CourseRequestDto courseRequestDto) {
        return new ResponseDto<CourseDto>(courseService.updateCourse(Long.valueOf(authentication.getName()), courseId, courseRequestDto));
    }

    // Course Delete
    @DeleteMapping("/{courseId}")
    public ResponseDto<Boolean> deleteCourse(Authentication authentication, @PathVariable Long courseId) {
        return new ResponseDto<Boolean>(courseService.deleteCourse(Long.valueOf(authentication.getName()), Long.valueOf(courseId)));
    }

    @GetMapping("/location")
    public ResponseDto<List<CourseDto>> getCourseListByLocations(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude) {
        return new ResponseDto<List<CourseDto>>(courseService.getCourseListByLocation(latitude, longitude));
    }

    @GetMapping("/tag")
    public ResponseDto<List<CourseDto>> getCourseListByTag(@RequestParam("name") String tag) {
        return new ResponseDto<List<CourseDto>>(courseService.getCourseListByTag(tag));
    }

    @PostMapping("/like/{courseId}")
    public ResponseDto<Map<String, Object>> likeCourse(Authentication authentication, @PathVariable Long courseId) {
        return new ResponseDto<Map<String, Object>>(courseService.likeCourse(Long.valueOf(authentication.getName()), courseId));
    }

    @DeleteMapping("/like/{courseId}")
    public ResponseDto<Map<String, Object>> dislikeCourse(Authentication authentication, @PathVariable Long courseId) {
        return new ResponseDto<Map<String, Object>>(courseService.dislikeCourse(Long.valueOf(authentication.getName()), courseId));
    }
}
