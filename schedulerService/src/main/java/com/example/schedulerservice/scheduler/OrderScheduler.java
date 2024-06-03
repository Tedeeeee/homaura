package com.example.schedulerservice.scheduler;

import com.example.schedulerservice.client.OrderServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderServiceClient orderServiceClient;

    // 하루 날짜 혹은 12시간을 기준으로 체크
    @Scheduled(fixedRate = 10000, initialDelay = 3000)
    public void checkRefundStatus() {
        orderServiceClient.updateStatus();
    }
}
