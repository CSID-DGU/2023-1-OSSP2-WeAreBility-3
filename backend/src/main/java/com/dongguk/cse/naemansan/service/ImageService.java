package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Image;
import com.dongguk.cse.naemansan.domain.type.ImageUseType;
import com.dongguk.cse.naemansan.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final ImageRepository imageRepository;

    private final String FOLDER_PATH="C:/Users/HyungJoon/Documents/0_OSSP/resources/images/";

    public String uploadImage(Long useId, ImageUseType imageUseType, MultipartFile file) throws IOException {
        log.info("이미지 저장 시작 유저: {} , 파일이름: {}", useId, file.getOriginalFilename());
        String uuidImageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String filePath = FOLDER_PATH + uuidImageName;

        try {
            file.transferTo(new File(filePath));
        } catch (Exception e) {
            log.error("파일 저장 실패 - {}", file.getOriginalFilename());
            return "file uploaded Fail : " + filePath;
        }

        Optional<Image> findImage = imageRepository.findByUserId(useId);

        if (findImage.isEmpty()) {
            imageRepository.save(Image.builder()
                    .useId(useId)
                    .imageUseType(imageUseType)
                    .originName(file.getOriginalFilename())
                    .uuidName(uuidImageName)
                    .type(file.getContentType())
                    .path(filePath).build());
        } else {
            File currentFile = new File(findImage.get().getPath());
            boolean result = currentFile.delete();

            findImage.get().setOriginName(file.getOriginalFilename());
            findImage.get().setUuidName(uuidImageName);
            findImage.get().setPath(filePath);
            findImage.get().setType(file.getContentType());
        }

        return "file uploaded successfully : " + filePath;
    }

    public byte[] downloadImage(String fileName) throws IOException {
        Optional<Image> image = imageRepository.findByUuidName(fileName);

        if (image.isEmpty()) {
            log.error("존재하지 않는 파일입니다 - UUID: {}", fileName);
            return null;
        }

        String filePath = image.get().getPath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
}
