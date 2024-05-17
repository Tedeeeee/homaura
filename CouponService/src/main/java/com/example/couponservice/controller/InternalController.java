package com.example.couponservice.controller;

import com.example.couponservice.Entity.Coupon;
import com.example.couponservice.repository.CouponRepository;
import com.example.couponservice.vo.SendCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final CouponRepository couponRepository;

    @GetMapping("")
    Coupon findCoupon(@RequestParam String couponUUID){
        return couponRepository.findByCouponUUID(couponUUID);
    }
}
