package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.dto.request.CommentRequestDto;
import com.dongguk.cse.naemansan.dto.response.CommentDto;
import com.dongguk.cse.naemansan.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CommentController {
    private final CommentService commentService;
    // Comment Create
    @PostMapping("/{courseId}/comment")
    public ResponseDto<Boolean> createComment(Authentication authentication, @PathVariable Long courseId, @RequestBody CommentRequestDto commentRequestDto){
        return new ResponseDto<Boolean>(commentService.createComment(Long.valueOf(authentication.getName()), courseId, commentRequestDto));
    }

    // Comment Read
    @GetMapping("/{courseId}/comment")
    public ResponseDto<List<CommentDto>> readComment(@PathVariable Long courseId , @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<CommentDto>>(commentService.readComment(courseId, page, num));
    }

    // Comment Update
    @PutMapping("/{courseId}/comment/{commentId}")
    public ResponseDto<Boolean> updateComment(Authentication authentication, @PathVariable Long courseId,
                                              @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseDto<Boolean>(commentService.updateComment(Long.valueOf(authentication.getName()), courseId, commentId, commentRequestDto));
    }

    // Comment Delete
    @DeleteMapping("/{courseId}/comment/{commentId}")
    public ResponseDto<Boolean> deleteComment(Authentication authentication, @PathVariable Long courseId, @PathVariable Long commentId) {
        return new ResponseDto<Boolean>(commentService.deleteComment(Long.valueOf(authentication.getName()), courseId, commentId));
    }
}
