package com.example.couponservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponScheduler {

    private final EventService eventService;

    @Scheduled(fixedDelay = 1000)
    public void eventScheduler() {
        eventService.enter();
        eventService.getOrder();
    }
}
