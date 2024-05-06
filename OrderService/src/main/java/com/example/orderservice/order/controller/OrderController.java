package com.example.orderservice.order.controller;

import com.example.orderservice.global.Service.RabbitMQService;
import com.example.orderservice.global.Service.RedisService;
import com.example.orderservice.order.dto.OrderDto;
import com.example.orderservice.order.entity.Content;
import com.example.orderservice.order.mapstruct.OrderMapStruct;
import com.example.orderservice.order.service.OrderService;
import com.example.orderservice.order.vo.RequestOrder;
import com.example.orderservice.order.vo.ResponseOrder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapStruct orderMapStruct;
    private final RedisService redisService;


    @PostMapping("")
    public ResponseEntity<Integer> createOrder(@Valid @RequestBody RequestOrder requestOrder, HttpServletRequest request) {
        OrderDto orderDto = orderMapStruct.changeDto(requestOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDto, request));
    }

    @GetMapping("")
    public ResponseEntity<List<ResponseOrder>> getOrders(HttpServletRequest request) {
        List<OrderDto> orderList = orderService.getOrderList(request);
        return ResponseEntity.status(HttpStatus.OK).body(orderMapStruct.changeResponseList(orderList));
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteOrder(@RequestParam String orderUUID) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(orderUUID));
    }

    @GetMapping("/refund")
    public ResponseEntity<String> refundOrder(@RequestParam String orderUUID) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.refundOrder(orderUUID));
    }

    // 예약 구매 메소드 처리
    @PostMapping("/unique")
    public ResponseEntity<ResponseOrder> createUniqueOrder(@Valid @RequestBody RequestOrder requestOrder, HttpServletRequest request) {
        OrderDto orderDto = orderMapStruct.changeDto(requestOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapStruct.changeResponse(orderService.createUniqueOrder(orderDto, request)));
    }

    @GetMapping("/statusPayment")
    public ResponseEntity<Integer> changePayment(@RequestParam String orderUUID) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.changePayment(orderUUID));
    }

    @GetMapping("/dfdf")
    public void sdf(@RequestParam String key) {
        redisService.upCount(key);
    }
}
