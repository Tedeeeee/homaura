package com.example.couponservice.controller;

import com.example.couponservice.Entity.Coupon;
import com.example.couponservice.repository.CouponRepository;
import com.example.couponservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final CouponRepository couponRepository;
    private final EventService eventService;
    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("")
    Coupon findCoupon(@RequestParam String couponUUID) {
        return couponRepository.findByCouponUUID(couponUUID);
    }

    @GetMapping("/checkEventTime")
    String checkTime() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Coupon> all = couponRepository.findAll();

        for (Coupon coupon : all) {
            LocalDateTime startTime = coupon.getStartTime();
            System.out.println("startTime = " + startTime);
            System.out.println("now = " + now);
            if (startTime.equals(now)) {
                System.out.println("쿠폰 생성");
                redisTemplate.opsForValue().set("event", coupon.getCouponUUID());
                return coupon.getName();
            }
        }
        return null;
    }

    @GetMapping("/startEvent")
    boolean startEvent() {
        boolean enter = eventService.enter();
        boolean order = eventService.getOrder();
        return enter || order;
    }
}
