package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.Advertisement;
import com.dongguk.cse.naemansan.domain.Image;
import com.dongguk.cse.naemansan.domain.Notice;
import com.dongguk.cse.naemansan.domain.Shop;
import com.dongguk.cse.naemansan.domain.type.ImageUseType;
import com.dongguk.cse.naemansan.dto.request.AdvertisementRequestDto;
import com.dongguk.cse.naemansan.dto.request.NoticeRequestDto;
import com.dongguk.cse.naemansan.dto.request.ShopRequestDto;
import com.dongguk.cse.naemansan.dto.response.AdvertisementDto;
import com.dongguk.cse.naemansan.dto.response.NoticeDetailDto;
import com.dongguk.cse.naemansan.dto.response.NoticeListDto;
import com.dongguk.cse.naemansan.dto.response.ShopDto;
import com.dongguk.cse.naemansan.repository.*;
import com.dongguk.cse.naemansan.util.CourseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommonService {
    private final ShopRepository shopRepository;
    private final AdvertisementRepository advertisementRepository;
    private final NoticeRepository noticeRepository;
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

    public List<ShopDto> readShopList(Double latitude, Double longitude, Long pageNum, Long Num) {
        Pageable paging = PageRequest.of(pageNum.intValue(), Num.intValue(), Sort.by(Sort.Direction.ASC, "radius"));
        Page<ShopRepository.ShopLocationForm> page =  shopRepository.findListByLocation(courseUtil.getLatLng2Point(latitude, longitude), paging);

        List<ShopDto> list = new ArrayList<>();
        for (ShopRepository.ShopLocationForm form : page.getContent()) {
            Shop shop = shopRepository.findById(form.getId())
                    .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_SHOP));

            list.add(ShopDto.builder()
                    .id(shop.getId())
                    .name(shop.getShopName())
                    .introduction(shop.getShopIntroduction())
                    .location(courseUtil.getPoint2PointDto(shop.getShopLocation()))
                    .image(shop.getImage())
                    .radius(form.getRadius()).build());
        }

        return list;
    }

    public List<AdvertisementDto> readAdvertisementList() {
        List<Advertisement> adsList = advertisementRepository.findAll();

        List<AdvertisementDto> list = new ArrayList<>();
        for (Advertisement ads : adsList) {
            list.add(AdvertisementDto.builder()
                    .id(ads.getId())
                    .name(ads.getEnterpriseName())
                    .url(ads.getEnterpriseUrl())
                    .image(ads.getImage()).build());
        }

        return list;
    }

    public Boolean createNotice(NoticeRequestDto requestDto) {
        noticeRepository.findByTitleAndStatus(requestDto.getTitle(), true)
                .ifPresent(notice -> { throw new RestApiException(ErrorCode.DUPLICATION_TITLE); });

        noticeRepository.save(Notice.builder()
                        .title(requestDto.getTitle())
                        .content(requestDto.getContent()).build());

        return Boolean.TRUE;
    }

    public NoticeDetailDto readNotice(Long noticeId) {
        Notice notice = noticeRepository.findByIdAndStatus(noticeId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_NOTICE));

        notice.incrementCnt();

        return NoticeDetailDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .created_date(notice.getCreatedDate())
                .read_cnt(notice.getReadCnt())
                .is_edit(notice.getIsEdit()).build();
    }

    public NoticeDetailDto updateNotice(Long noticeId, NoticeRequestDto requestDto) {
        Notice notice = noticeRepository.findByIdAndStatus(noticeId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_NOTICE));
        noticeRepository.findByIdNotAndTitleAndStatus(noticeId, requestDto.getTitle(), true)
                .ifPresent(c -> { throw new RestApiException(ErrorCode.DUPLICATION_TITLE);});

        notice.setTitle(requestDto.getTitle());
        notice.setContent(requestDto.getContent());

        return NoticeDetailDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .created_date(notice.getCreatedDate())
                .read_cnt(notice.getReadCnt())
                .is_edit(notice.getIsEdit()).build();
    }

    public Boolean deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findByIdAndStatus(noticeId, true)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_NOTICE));

        notice.setStatus(false);

        return Boolean.TRUE;
    }

    public List<NoticeListDto> readNoticeList(Long pageIndex, Long maxNum) {
        Pageable paging = PageRequest.of(pageIndex.intValue(), maxNum.intValue(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Notice> page = noticeRepository.findAllByStatus(true, paging);

        List<NoticeListDto> list = new ArrayList<>();
        for (Notice notice : page.getContent()) {
            list.add(NoticeListDto.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .created_date(notice.getCreatedDate())
                    .read_cnt(notice.getReadCnt()).build());
        }

        return list;
    }
}
