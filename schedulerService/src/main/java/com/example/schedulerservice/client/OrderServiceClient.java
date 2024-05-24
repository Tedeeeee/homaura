package com.example.schedulerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "orderService")
public interface OrderServiceClient {
    @GetMapping("/internal")
    void updateStatus();
}
