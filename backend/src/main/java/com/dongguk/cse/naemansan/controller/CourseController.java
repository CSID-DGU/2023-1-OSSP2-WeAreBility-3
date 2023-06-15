package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.domain.CourseTag;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import com.dongguk.cse.naemansan.dto.request.IndividualCourseRequestDto;
import com.dongguk.cse.naemansan.dto.response.*;
import com.dongguk.cse.naemansan.dto.request.EnrollmentCourseRequestDto;
import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    // Individual Course Create
    @PostMapping("/individual")
    public ResponseDto<IndividualCourseDetailDto> createIndividualCourse(Authentication authentication, @RequestBody IndividualCourseRequestDto requestDto){
        return new ResponseDto<IndividualCourseDetailDto>(courseService.createIndividualCourse(Long.valueOf(authentication.getName()), requestDto));
    }

    // Individual Course Read
    @GetMapping("/individual/{courseId}")
    public ResponseDto<IndividualCourseDetailDto> readIndividualCourse(Authentication authentication, @PathVariable Long courseId){
        return new ResponseDto<IndividualCourseDetailDto>(courseService.readIndividualCourse(Long.valueOf(authentication.getName()), courseId));
    }

    // Individual Course Update
    @PutMapping("/individual/{courseId}")
    public ResponseDto<Boolean> updateIndividualCourse(Authentication authentication, @PathVariable Long courseId){
        return new ResponseDto<Boolean>(courseService.updateIndividualCourse(Long.valueOf(authentication.getName()), courseId));
    }

    // Individual Course Delete
    @DeleteMapping("/individual/{courseId}")
    public ResponseDto<Boolean> deleteIndividualCourse(Authentication authentication, @PathVariable Long courseId){
        return new ResponseDto<Boolean>(courseService.deleteIndividualCourse(Long.valueOf(authentication.getName()), courseId));
    }

    // Course Create
    @PostMapping("/enrollment")
    public ResponseDto<EnrollmentCourseDetailDto> createCourse(Authentication authentication, @RequestBody EnrollmentCourseRequestDto enrollmentCourseRequestDto){
        return new ResponseDto<EnrollmentCourseDetailDto>(courseService.createEnrollmentCourse(Long.valueOf(authentication.getName()), enrollmentCourseRequestDto));
    }

    // Course Read
    @GetMapping("/enrollment/{courseId}")
    public ResponseDto<EnrollmentCourseDetailDto> readCourse(Authentication authentication, @PathVariable Long courseId) {
        return new ResponseDto<EnrollmentCourseDetailDto>(courseService.readEnrollmentCourse(Long.valueOf(authentication.getName()), Long.valueOf(courseId)));
    }

    // Course Update
    @PutMapping("/enrollment/{courseId}")
    public ResponseDto<EnrollmentCourseDetailDto> updateCourse(Authentication authentication, @PathVariable Long courseId, @RequestBody EnrollmentCourseRequestDto enrollmentCourseRequestDto) {
        return new ResponseDto<EnrollmentCourseDetailDto>(courseService.updateEnrollmentCourse(Long.valueOf(authentication.getName()), courseId, enrollmentCourseRequestDto));
    }

    // Course Delete
    @DeleteMapping("/enrollment/{courseId}")
    public ResponseDto<Boolean> deleteCourse(Authentication authentication, @PathVariable Long courseId) {
        return new ResponseDto<Boolean>(courseService.deleteEnrollmentCourse(Long.valueOf(authentication.getName()), Long.valueOf(courseId)));
    }

    // Using Course Create
    @PostMapping("/using")
    public ResponseDto<Boolean> createUsingCourse(Authentication authentication, @RequestBody UsingCourseRequestDto requestDto){
        return new ResponseDto<Boolean>(courseService.createUsingCourse(Long.valueOf(authentication.getName()), requestDto));
    }

    @GetMapping("/list/main/tag")
    public ResponseDto<List<EnrollmentCourseListDto>> getEnrollmentCourseListByTagForMain(Authentication authentication, @RequestParam("name") String tag) {
        return new ResponseDto<List<EnrollmentCourseListDto>>(courseService.getEnrollmentCourseListByTag(Long.valueOf(authentication.getName()), 0L, 5L, tag));
    }

    @GetMapping("/list/main/location")
    public ResponseDto<List<EnrollmentCourseListDto>> getEnrollmentCourseListByLocationsForMain(Authentication authentication, @RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude) {
        return new ResponseDto<List<EnrollmentCourseListDto>>(courseService.getEnrollmentCourseListByLocation(Long.valueOf(authentication.getName()), 0L, 5L, latitude, longitude));
    }

    @GetMapping("/list/individual/basic")
    public ResponseDto<List<IndividualCourseListDto>> getIndividualCourseList(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<IndividualCourseListDto>>(courseService.getIndividualCourseList(Long.valueOf(authentication.getName()), page, num));
    }

    @GetMapping("/list/individual/enrollment")
    public ResponseDto<List<EnrollmentCourseListDto>> getEnrollmentCourseListByUser(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<EnrollmentCourseListDto>>(courseService.getEnrollmentCourseListByUser(Long.valueOf(authentication.getName()), page, num));
    }

    @GetMapping("/list/individual/like")
    public ResponseDto<List<EnrollmentCourseListDto>> getEnrollmentCourseListByLikeAndUser(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<EnrollmentCourseListDto>>(courseService.getEnrollmentCourseListByLikeAndUser(Long.valueOf(authentication.getName()), page, num));
    }

    @GetMapping("/list/individual/using")
    public ResponseDto<List<EnrollmentCourseListDto>> getEnrollmentCourseListByUsingAndUser(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<EnrollmentCourseListDto>>(courseService.getEnrollmentCourseListByUsingAndUser(Long.valueOf(authentication.getName()), page, num));
    }

    @GetMapping("/list/individual/tag")
    public ResponseDto<List<EnrollmentCourseListDto>> getEnrollmentCourseListByTag(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num,
                                                                         @RequestParam("name") String tag) {
        return new ResponseDto<List<EnrollmentCourseListDto>>(courseService.getEnrollmentCourseListByTag(Long.valueOf(authentication.getName()), page, num, tag));
    }

    // 서버 연동 시 사용가능
    @GetMapping("/list/recommend")
    public ResponseDto<List<EnrollmentCourseListDto>> getEnrollmentCourseListByUsingCount(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<EnrollmentCourseListDto>>(courseService.getEnrollmentCourseListByRecommend(Long.valueOf(authentication.getName()), page, num));
    }

    @GetMapping("/list/all")
    public ResponseDto<List<EnrollmentCourseListDto>> getEnrollmentCourseListByRecommend(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<EnrollmentCourseListDto>>(courseService.getEnrollmentCourseList(Long.valueOf(authentication.getName()), page, num));
    }

    @GetMapping("/list/like")
    public ResponseDto<List<EnrollmentCourseListDto>> getEnrollmentCourseList(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<EnrollmentCourseListDto>>(courseService.getEnrollmentCourseListByLikeCount(Long.valueOf(authentication.getName()), page, num));
    }

    @GetMapping("/list/using")
    public ResponseDto<List<EnrollmentCourseListDto>> getEnrollmentCourseListByLikeCount(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<EnrollmentCourseListDto>>(courseService.getEnrollmentCourseListByUsingCount(Long.valueOf(authentication.getName()), page, num));
    }

    @GetMapping("/list/location")
    public ResponseDto<List<EnrollmentCourseListDto>> getEnrollmentCourseListByLocation(Authentication authentication, @RequestParam("page") Long page, @RequestParam("num") Long num,
                                                                               @RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude) {
        return new ResponseDto<List<EnrollmentCourseListDto>>(courseService.getEnrollmentCourseListByLocation(Long.valueOf(authentication.getName()), page, num, latitude, longitude));
    }

    @PostMapping("/{courseId}/like")
    public ResponseDto<Map<String, Object>> likeCourse(Authentication authentication, @PathVariable Long courseId) {
        return new ResponseDto<Map<String, Object>>(courseService.likeCourse(Long.valueOf(authentication.getName()), courseId));
    }

    @DeleteMapping("/{courseId}/like")
    public ResponseDto<Map<String, Object>> dislikeCourse(Authentication authentication, @PathVariable Long courseId) {
        return new ResponseDto<Map<String, Object>>(courseService.dislikeCourse(Long.valueOf(authentication.getName()), courseId));
    }

    @GetMapping("/tags")
    public ResponseDto<?> getTagList() {
        return new ResponseDto<List<CourseTagType>>(courseService.getTagList());
    }
}
