package com.example.couponservice.controller;

import com.example.couponservice.dto.CouponDto;
import com.example.couponservice.mapstruct.CouponMapStruct;
import com.example.couponservice.service.CouponService;
import com.example.couponservice.vo.RequestCoupon;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final CouponMapStruct couponMapStruct;

    // 쿠폰 발행
    @PostMapping("")
    public ResponseEntity<Integer> createCoupon(@RequestBody RequestCoupon requestCoupon) {
        CouponDto couponDto = couponMapStruct.changeDto(requestCoupon);
        return new ResponseEntity<>(couponService.createCoupon(couponDto), HttpStatus.OK);
    }

    // 쿠폰 발급

}
