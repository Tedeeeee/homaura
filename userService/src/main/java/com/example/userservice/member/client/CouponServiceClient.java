package com.example.userservice.member.client;

import com.example.userservice.member.vo.Coupon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "couponService")
public interface CouponServiceClient {

    @GetMapping("/internal")
    Coupon findCoupon(@RequestParam String couponUUID);
}
