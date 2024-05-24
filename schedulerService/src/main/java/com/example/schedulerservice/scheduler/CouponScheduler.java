package com.example.schedulerservice.scheduler;

import com.example.schedulerservice.client.CouponServiceClient;
import com.example.schedulerservice.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponScheduler {

    private final CouponServiceClient couponServiceClient;

    private List<String> couponList = new ArrayList<>();

    @Scheduled(cron = "0 */1 * * * *")
    public void eventCheck() {
        System.out.println("이벤트 체크 중");
        String s = couponServiceClient.checkTime();
        if (s != null) couponList.add(s);
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 3000)
    public void eventScheduler() {
        if (couponList.size() != 0) {
            System.out.println(couponList.get(0) + "쿠폰 이벤트 시작");
            boolean end = couponServiceClient.startEvent();
            System.out.println("end = " + end);
            if (!end) couponList.remove(0);
        }
    }
}
