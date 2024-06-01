package com.example.schedulerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "couponService")
public interface CouponServiceClient {
    @GetMapping("/internal/checkEventTime")
    String checkTime();

    @GetMapping("/internal/startEvent")
    boolean startEvent();
}
