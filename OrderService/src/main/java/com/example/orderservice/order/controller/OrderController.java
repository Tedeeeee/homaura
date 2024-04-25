package com.example.orderservice.order.controller;

import com.example.orderservice.order.dto.OrderDto;
import com.example.orderservice.order.mapstruct.OrderMapStruct;
import com.example.orderservice.order.service.OrderService;
import com.example.orderservice.order.vo.RequestOrder;
import com.example.orderservice.order.vo.ResponseOrder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapStruct orderMapStruct;

    @PostMapping("")
    public ResponseEntity<Integer> createOrder(@Valid @RequestBody RequestOrder requestOrder) {
        OrderDto orderDto = orderMapStruct.changeDto(requestOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDto));
    }

    @GetMapping("")
    public ResponseEntity<List<ResponseOrder>> getOrders() {
        List<OrderDto> orderList = orderService.getOrderList();
        return ResponseEntity.status(HttpStatus.OK).body(orderMapStruct.changeResponseList(orderList));
    }

    @DeleteMapping("/{orderUUId}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderUUId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(orderUUId));
    }

    @GetMapping("/{orderUUId}/refund")
    public ResponseEntity<String> refundOrder(@PathVariable String orderUUId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.refundOrder(orderUUId));
    }
}
