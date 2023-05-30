package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.dto.request.AdvertisementRequestDto;
import com.dongguk.cse.naemansan.dto.request.ShopRequestDto;
import com.dongguk.cse.naemansan.dto.response.AdvertisementDto;
import com.dongguk.cse.naemansan.dto.response.ShopDto;
import com.dongguk.cse.naemansan.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/shop")
    public ResponseDto<?> createShopProfile(@RequestBody ShopRequestDto requestDto) {
        return new ResponseDto<Boolean>(adminService.createShopProfile(requestDto));
    }

    @GetMapping("/shop/{shopId}")
    public ResponseDto<?> readShopProfile(@PathVariable Long shopId) {
        return new ResponseDto<ShopDto>(adminService.readShopProfile(shopId));
    }

    @PutMapping("/shop/{shopId}")
    public ResponseDto<?> updateShopProfile(@PathVariable Long shopId, @RequestBody ShopRequestDto requestDto) {
        return new ResponseDto<ShopDto>(adminService.updateShopProfile(shopId, requestDto));
    }

    @DeleteMapping("/shop/{shopId}")
    public ResponseDto<?> deleteShopProfile(@PathVariable Long shopId) {
        return new ResponseDto<Boolean>(adminService.deleteShopProfile(shopId));
    }

    @PostMapping("/enterprise")
    public ResponseDto<?> createEnterpriseProfile(@RequestBody AdvertisementRequestDto requestDto) {
        return new ResponseDto<Boolean>(adminService.createAdvertisementProfile(requestDto));
    }

    @GetMapping("/enterprise/{enterpriseId}")
    public ResponseDto<?> readEnterpriseProfile(@PathVariable Long enterpriseId) {
        return new ResponseDto<AdvertisementDto>(adminService.readAdvertisementProfile(enterpriseId));
    }

    @PutMapping("/enterprise/{enterpriseId}")
    public ResponseDto<?> updateEnterpriseProfile(@PathVariable Long enterpriseId, @RequestBody AdvertisementRequestDto requestDto) {
        return new ResponseDto<AdvertisementDto>(adminService.updateAdvertisementProfile(enterpriseId, requestDto));
    }

    @DeleteMapping("/enterprise/{enterpriseId}")
    public ResponseDto<?> deleteEnterpriseProfile(@PathVariable Long enterpriseId) {
        return new ResponseDto<Boolean>(adminService.deleteAdvertisementProfile(enterpriseId));
    }
}
