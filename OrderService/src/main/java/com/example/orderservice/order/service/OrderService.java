package com.example.orderservice.order.service;


import com.example.orderservice.order.dto.OrderDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {
    int createOrder(OrderDto orderDto, HttpServletRequest request);

    List<OrderDto> getOrderList(HttpServletRequest request);

    String deleteOrder(String orderUUID);

    String refundOrder(String orderUUID);

    // 예약 주문 전용 메소드
    OrderDto createUniqueOrder(OrderDto orderDto, HttpServletRequest request);

    int changePayment(String orderUUID);
}
