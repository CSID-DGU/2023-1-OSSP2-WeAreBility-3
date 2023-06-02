package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.dto.request.AdvertisementRequestDto;
import com.dongguk.cse.naemansan.dto.request.NoticeRequestDto;
import com.dongguk.cse.naemansan.dto.request.ShopRequestDto;
import com.dongguk.cse.naemansan.dto.response.AdvertisementDto;
import com.dongguk.cse.naemansan.dto.response.NoticeDetailDto;
import com.dongguk.cse.naemansan.dto.response.ShopDto;
import com.dongguk.cse.naemansan.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final CommonService commonService;

    @PostMapping("/shop")
    public ResponseDto<?> createShopProfile(@RequestBody ShopRequestDto requestDto) {
        return new ResponseDto<Boolean>(commonService.createShopProfile(requestDto));
    }

    @PutMapping("/shop/{shopId}")
    public ResponseDto<?> updateShopProfile(@PathVariable Long shopId, @RequestBody ShopRequestDto requestDto) {
        return new ResponseDto<ShopDto>(commonService.updateShopProfile(shopId, requestDto));
    }

    @DeleteMapping("/shop/{shopId}")
    public ResponseDto<?> deleteShopProfile(@PathVariable Long shopId) {
        return new ResponseDto<Boolean>(commonService.deleteShopProfile(shopId));
    }

    @PostMapping("/enterprise")
    public ResponseDto<?> createEnterpriseProfile(@RequestBody AdvertisementRequestDto requestDto) {
        return new ResponseDto<Boolean>(commonService.createAdvertisementProfile(requestDto));
    }

    @PutMapping("/enterprise/{enterpriseId}")
    public ResponseDto<?> updateEnterpriseProfile(@PathVariable Long enterpriseId, @RequestBody AdvertisementRequestDto requestDto) {
        return new ResponseDto<AdvertisementDto>(commonService.updateAdvertisementProfile(enterpriseId, requestDto));
    }

    @DeleteMapping("/enterprise/{enterpriseId}")
    public ResponseDto<?> deleteEnterpriseProfile(@PathVariable Long enterpriseId) {
        return new ResponseDto<Boolean>(commonService.deleteAdvertisementProfile(enterpriseId));
    }

    @PostMapping("/notice")
    public ResponseDto<?> createNotice(@RequestBody NoticeRequestDto requestDto) {
        return new ResponseDto<Boolean>(commonService.createNotice(requestDto));
    }

    @PutMapping("/notice/{noticeId}")
    public ResponseDto<?> updateNotice(@PathVariable Long noticeId, @RequestBody NoticeRequestDto requestDto) {
        return new ResponseDto<NoticeDetailDto>(commonService.updateNotice(noticeId, requestDto));
    }

    @DeleteMapping("/notice/{noticeId}")
    public ResponseDto<?> deleteNotice(@PathVariable Long noticeId) {
        return new ResponseDto<Boolean>(commonService.deleteNotice(noticeId));
    }
}
