package com.shoppingmall.homaura.order.controller;

import com.shoppingmall.homaura.order.dto.OrderDto;
import com.shoppingmall.homaura.order.mapstruct.OrderMapStruct;
import com.shoppingmall.homaura.order.service.OrderService;
import com.shoppingmall.homaura.order.vo.RequestOrder;
import com.shoppingmall.homaura.order.vo.ResponseOrder;
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
    public ResponseEntity<String> createOrder(@Valid @RequestBody RequestOrder requestOrder) {
        OrderDto orderDto = orderMapStruct.changeDto(requestOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDto));
    }

    @GetMapping("")
    public ResponseEntity<List<ResponseOrder>> getOrders(@RequestParam String memberUUID) {
        List<OrderDto> orderList = orderService.getOrderList(memberUUID);
        return ResponseEntity.status(HttpStatus.OK).body(orderMapStruct.changeResponseList(orderList));
    }

    @DeleteMapping("/{orderUUId}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderUUId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(orderUUId));
    }
}
