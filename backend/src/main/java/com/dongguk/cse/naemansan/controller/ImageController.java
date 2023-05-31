package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.common.ExceptionDto;
import com.dongguk.cse.naemansan.domain.type.ImageUseType;
import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.service.ImageService;
import com.sun.nio.sctp.IllegalUnbindException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/image")
    public ResponseEntity<?> downloadImage(@RequestParam("uuid") String fileName) throws IOException {
        byte[] imageData = imageService.downloadImage(fileName);

        // 임시 체크
        if (imageData.length == 0)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.builder()
                            .success(false)
                            .data(null)
                            .error(new ExceptionDto(new IllegalUnbindException())).build());
        else
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(imageData);
    }

    @PostMapping("/image/user")
    public ResponseDto<?> uploadUserImage(Authentication authentication, @RequestParam("image") MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("uuid_name", imageService.uploadImage(Long.valueOf(authentication.getName()), ImageUseType.USER, file));
        return new ResponseDto(map);
    }

    @PostMapping("/admin/image/shop/{shopId}")
    public ResponseDto<?> uploadShopImage(@PathVariable Long shopId, @RequestParam("image") MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("uuid_name", imageService.uploadImage(shopId, ImageUseType.SHOP, file));
        return new ResponseDto(map);
    }

    @PostMapping("/admin/image/enterprise/{advertisementId}")
    public ResponseDto<?> uploadAdvertisementImage(@PathVariable Long advertisementId, @RequestParam("image")MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("uuid_name", imageService.uploadImage(advertisementId, ImageUseType.ADVERTISEMENT, file));
        return new ResponseDto(map);
    }

}
