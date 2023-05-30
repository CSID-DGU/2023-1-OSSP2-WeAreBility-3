package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.dto.response.AdvertisementDto;
import com.dongguk.cse.naemansan.dto.response.ShopDto;
import com.dongguk.cse.naemansan.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommonController {
    private final CommonService commonService;

    @GetMapping("/shop")
    public ResponseDto<?> readShopList(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude,
                                          @RequestParam("page") Long page, @RequestParam("num") Long num) {
        return new ResponseDto<List<ShopDto>>(commonService.readShopList(latitude, longitude, page, num));
    }

    @GetMapping("/enterprise")
    public ResponseDto<?> readEnterpriseProfile() {
        return new ResponseDto<List<AdvertisementDto>>(commonService.readAdvertisementList());
    }
}
