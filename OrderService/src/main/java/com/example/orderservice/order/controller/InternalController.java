package com.example.orderservice.order.controller;


import com.example.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final OrderService orderService;

    @GetMapping("")
    void updateStatus() {
        orderService.changeStatus();
    }
}
