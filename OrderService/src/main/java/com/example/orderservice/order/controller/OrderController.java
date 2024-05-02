package com.example.orderservice.order.controller;

import com.example.orderservice.order.dto.OrderDto;
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
}