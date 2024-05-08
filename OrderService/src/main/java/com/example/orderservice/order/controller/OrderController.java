package com.example.orderservice.order.controller;

import com.example.orderservice.wishList.service.RedisService;
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


    // 상세 정보 입력 후 주문하기
    // 선착순 이벤트 주문하기
    @PostMapping("/unique")
    public ResponseEntity<String> createOrder(@Valid @RequestBody RequestOrder requestOrder, HttpServletRequest request) {
        OrderDto orderDto = orderMapStruct.changeDto(requestOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDto, request));
    }

    // 일반 주문
    @PostMapping("")
    public ResponseEntity<String> createOrders(@Valid @RequestBody RequestOrder requestOrder, HttpServletRequest request) {
        OrderDto orderDto = orderMapStruct.changeDto(requestOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrders(orderDto, request));
    }

    // 나의 주문 리스트 확인
    @GetMapping("")
    public ResponseEntity<List<ResponseOrder>> getOrders(HttpServletRequest request) {
        List<OrderDto> orderList = orderService.getOrderList(request);
        return ResponseEntity.status(HttpStatus.OK).body(orderMapStruct.changeResponseList(orderList));
    }

    // 주문 취소
    @DeleteMapping("")
    public ResponseEntity<String> deleteOrder(@RequestParam String orderUUID) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(orderUUID));
    }

    // 반품 하기
    @GetMapping("/refund")
    public ResponseEntity<String> refundOrder(@RequestParam String orderUUID) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.refundOrder(orderUUID));
    }

    // 결제 완료
    @GetMapping("/statusPayment")
    public ResponseEntity<Integer> changePayment(@RequestParam String orderUUID) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.changePayment(orderUUID));
    }
}
