package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.Shop;
import com.dongguk.cse.naemansan.dto.request.ShopRequestDto;
import com.dongguk.cse.naemansan.dto.response.ShopDto;
import com.dongguk.cse.naemansan.repository.AdvertisementRepository;
import com.dongguk.cse.naemansan.repository.ShopRepository;
import com.dongguk.cse.naemansan.util.CourseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final ShopRepository shopRepository;
    private final AdvertisementRepository advertisementRepository;
    private final CourseUtil courseUtil;

    public Boolean createShopProfile(ShopRequestDto requestDto) {
        shopRepository.findByShopName(requestDto.getName())
                .ifPresent(shop -> { throw new RestApiException(ErrorCode.DUPLICATION_NAME); });

        Shop shop = shopRepository.save(Shop.builder()
                        .shopName(requestDto.getName())
                        .shopIntroduction(requestDto.getIntroduction())
                        .shopLocation(courseUtil.getPointDto2Point(requestDto.getLocation())).build());
        return Boolean.TRUE;
    }

    public ShopDto readShopProfile(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_SHOP));

        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getShopName())
                .introduction(shop.getShopIntroduction())
                .location(courseUtil.getPoint2PointDto(shop.getShopLocation())).build();
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
                .location(courseUtil.getPoint2PointDto(shop.getShopLocation())).build();
    }

    public Boolean deleteShopProfile(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_SHOP));

        shopRepository.delete(shop);

        return Boolean.TRUE;
    }
}
