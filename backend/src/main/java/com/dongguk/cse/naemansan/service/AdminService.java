package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.Advertisement;
import com.dongguk.cse.naemansan.domain.Image;
import com.dongguk.cse.naemansan.domain.Shop;
import com.dongguk.cse.naemansan.domain.type.ImageUseType;
import com.dongguk.cse.naemansan.dto.request.AdvertisementRequestDto;
import com.dongguk.cse.naemansan.dto.request.ShopRequestDto;
import com.dongguk.cse.naemansan.dto.response.AdvertisementDto;
import com.dongguk.cse.naemansan.dto.response.ShopDto;
import com.dongguk.cse.naemansan.repository.AdvertisementRepository;
import com.dongguk.cse.naemansan.repository.ImageRepository;
import com.dongguk.cse.naemansan.repository.ShopRepository;
import com.dongguk.cse.naemansan.util.CourseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final ShopRepository shopRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ImageRepository imageRepository;
    private final CourseUtil courseUtil;

    @Value("${spring.image.path: aaa.bbb.ccc}")
    private String FOLDER_PATH;

    public Boolean createShopProfile(ShopRequestDto requestDto) {
        shopRepository.findByShopName(requestDto.getName())
                .ifPresent(shop -> { throw new RestApiException(ErrorCode.DUPLICATION_NAME); });

        Shop shop = shopRepository.save(Shop.builder()
                        .shopName(requestDto.getName())
                        .shopIntroduction(requestDto.getIntroduction())
                        .shopLocation(courseUtil.getPointDto2Point(requestDto.getLocation())).build());
        imageRepository.save(Image.builder()
                .useObject(shop)
                .imageUseType(ImageUseType.SHOP)
                .originName("default_image.png")
                .uuidName("0_default_image.png")
                .type("image/png")
                .path(FOLDER_PATH + "0_default_image.png").build());

        return Boolean.TRUE;
    }

    public ShopDto readShopProfile(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_SHOP));

        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getShopName())
                .introduction(shop.getShopIntroduction())
                .location(courseUtil.getPoint2PointDto(shop.getShopLocation()))
                .image(shop.getImage()).build();
    }

    public ShopDto updateShopProfile(Long shopId, ShopRequestDto requestDto) {
        shopRepository.findByIdNotAndShopName(shopId, requestDto.getName())
                .ifPresent(shop -> { throw new RestApiException(ErrorCode.DUPLICATION_NAME); });
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_SHOP));

        shop.setShopName(requestDto.getName());
        shop.setShopIntroduction(requestDto.getIntroduction());
        shop.setShopLocation(courseUtil.getPointDto2Point(requestDto.getLocation()));

        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getShopName())
                .introduction(shop.getShopIntroduction())
                .location(courseUtil.getPoint2PointDto(shop.getShopLocation()))
                .image(shop.getImage()).build();
    }

    public Boolean deleteShopProfile(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_SHOP));

        shopRepository.delete(shop);

        return Boolean.TRUE;
    }

    public Boolean createAdvertisementProfile(AdvertisementRequestDto requestDto) {
        advertisementRepository.findByEnterpriseName(requestDto.getName())
                .ifPresent(ads -> { throw new RestApiException(ErrorCode.DUPLICATION_NAME); });

        Advertisement advertisement = advertisementRepository.save(Advertisement.builder()
                .enterpriseName(requestDto.getName())
                .enterpriseUrl(requestDto.getUrl()).build());

        imageRepository.save(Image.builder()
                .useObject(advertisement)
                .imageUseType(ImageUseType.ADVERTISEMENT)
                .originName("default_image.png")
                .uuidName("0_default_image.png")
                .type("image/png")
                .path(FOLDER_PATH + "0_default_image.png").build());

        return Boolean.TRUE;
    }

    public AdvertisementDto readAdvertisementProfile(Long advertisementId) {
        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ADVERTISEMENT));

        return AdvertisementDto.builder()
                .id(advertisement.getId())
                .name(advertisement.getEnterpriseName())
                .url(advertisement.getEnterpriseUrl())
                .image(advertisement.getImage()).build();
    }

    public AdvertisementDto updateAdvertisementProfile(Long advertisementId, AdvertisementRequestDto requestDto) {
        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ADVERTISEMENT));

        advertisement.setEnterpriseName(requestDto.getName());
        advertisement.setEnterpriseUrl(requestDto.getUrl());

        return AdvertisementDto.builder()
                .id(advertisement.getId())
                .name(advertisement.getEnterpriseName())
                .url(advertisement.getEnterpriseUrl())
                .image(advertisement.getImage()).build();
    }

    public Boolean deleteAdvertisementProfile(Long advertisementId) {
        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_ADVERTISEMENT));

        advertisementRepository.delete(advertisement);

        return Boolean.TRUE;
    }
}
