package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.Image;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.domain.type.ImageUseType;
import com.dongguk.cse.naemansan.repository.AdvertisementRepository;
import com.dongguk.cse.naemansan.repository.ImageRepository;
import com.dongguk.cse.naemansan.repository.ShopRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ImageRepository imageRepository;

    @Value("${spring.image.path}")
    private String FOLDER_PATH;

    public String uploadImage(Long useId, ImageUseType imageUseType, MultipartFile file) throws IOException {
        // File Path Fetch
        String uuidImageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String filePath = FOLDER_PATH + uuidImageName;

        // File Upload
        try {
            file.transferTo(new File(filePath));
        } catch (Exception e) {
            throw new RestApiException(ErrorCode.FILE_UPLOAD);
        }

        // Path DB Save
        Optional<? extends Object> useObject = null;
        switch (imageUseType) {
            case USER -> { useObject = userRepository.findById(useId); }
            case SHOP -> { useObject = shopRepository.findById(useId); }
            case ADVERTISEMENT -> { useObject = advertisementRepository.findById(useId); }
        }

        // 기존 파일이 없다면 새롭게 추가, 아니라면 기존 파일 삭제 후 저장
        Optional<Image> findImage = imageRepository.findByUser((User) useObject.get());
        if (findImage.isEmpty()) {
            imageRepository.save(Image.builder()
                    .useObject(useObject)
                    .imageUseType(imageUseType)
                    .originName(file.getOriginalFilename())
                    .uuidName(uuidImageName)
                    .type(file.getContentType())
                    .path(filePath).build());
        } else {
            if (!findImage.get().getOriginName().equals("0_default_image.png")) {
                File currentFile = new File(findImage.get().getPath());
                boolean result = currentFile.delete();
            }

            findImage.get().updateImage(file.getOriginalFilename(), uuidImageName, filePath, file.getContentType());
        }

        return uuidImageName;
    }

    public byte[] downloadImage(String UuidName) throws IOException {
        String filePath = null;
        Image image = null;

        if (UuidName.equals("0_default_image.png")) {
            filePath = FOLDER_PATH + "0_default_image.png";
        } else {
            image = imageRepository.findByUuidName(UuidName).orElseThrow(() -> new RestApiException(ErrorCode.FILE_DOWNLOAD));
            filePath = image.getPath();

        }

        log.info(filePath);
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
}
