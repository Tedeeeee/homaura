package com.example.orderservice.order.service;


import com.example.orderservice.order.dto.OrderDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {
    int createOrder(OrderDto orderDto, HttpServletRequest request);

    List<OrderDto> getOrderList(HttpServletRequest request);

    String deleteOrder(String orderUUID);

    String refundOrder(String orderUUID);

    int changePayment(String orderUUID);
}
