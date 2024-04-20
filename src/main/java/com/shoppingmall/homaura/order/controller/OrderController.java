package com.shoppingmall.homaura.order.controller;

import com.shoppingmall.homaura.order.dto.OrderDto;
import com.shoppingmall.homaura.order.mapstruct.OrderMapStruct;
import com.shoppingmall.homaura.order.service.OrderService;
import com.shoppingmall.homaura.order.vo.RequestOrder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapStruct orderMapStruct;

    @PostMapping("")
    public ResponseEntity<String> createOrder(@Valid @RequestBody RequestOrder requestOrder) {
        OrderDto orderDto = orderMapStruct.changeDto(requestOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDto));
    }
}
