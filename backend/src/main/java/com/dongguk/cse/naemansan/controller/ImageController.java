package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.domain.type.ImageUseType;
import com.dongguk.cse.naemansan.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/user")
    public ResponseEntity<?> uploadImage(Authentication authentication, @RequestParam("image")MultipartFile file) throws IOException {
        log.info("이미지 저장 요청 | User: {}", Long.valueOf(authentication.getName()));
        String uploadImage = imageService.uploadImage(Long.valueOf(authentication.getName()), ImageUseType.USER, file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) throws IOException {
        return null;
    }
}
