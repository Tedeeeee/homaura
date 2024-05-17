package com.example.couponservice;

import com.example.couponservice.dto.CouponDto;
import com.example.couponservice.service.CouponService;
import com.example.couponservice.service.EventService;
import com.example.couponservice.vo.RequestCoupon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class CouponServiceApplicationTests {

    @Autowired
    private CouponService couponService;

    @Autowired
    private EventService eventService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @BeforeEach
    void setUp() {
        couponService.createCoupon(couponDto());
    }

    @Test
    @DisplayName("선착순 30명 이벤트에 100명이 접속하면 70명의 대기인원이 남는다.")
    void event() throws InterruptedException {
        int numThreads = 100;

        final CountDownLatch countDownLatch = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            int number = i + 1;
            executorService.execute(() -> {
                try {
                    eventService.eventStart("asdf", number + "번째 회원");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();
    }

    private CouponDto couponDto() {
        CouponDto couponDto = new CouponDto();
        couponDto.setName("3000원 쿠폰");
        couponDto.setDiscount(3000);
        couponDto.setQuantity(34);
        couponDto.setStartTime("2024-05-13T06:00");
        return couponDto;
    }

}
