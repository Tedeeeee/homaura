package com.example.couponservice.service;

import com.example.couponservice.Entity.Coupon;
import com.example.couponservice.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponScheduler {

    private final EventService eventService;
    private final CouponRepository couponRepository;
    private final RedisTemplate<String, String> redisTemplate;
    public static boolean flag;

    @Scheduled(fixedDelay = 1000, initialDelay = 3000)
    public void eventScheduler() {
        if (flag) {
            System.out.println("쿠폰 이벤트 중");
            eventService.enter();
            eventService.getOrder();
        }
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void startEventTime() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Coupon> all = couponRepository.findAll();

        for (Coupon coupon : all) {
            LocalDateTime startTime = coupon.getStartTime();
            if (startTime.equals(now)) {
                redisTemplate.opsForValue().set("event", coupon.getCouponUUID());
                flag = true;
                break;
            }
        }
    }
}
