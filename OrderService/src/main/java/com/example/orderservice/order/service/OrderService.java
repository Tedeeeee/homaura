package com.example.orderservice.order.service;


import com.example.orderservice.order.dto.OrderDto;

import java.util.List;

public interface OrderService {
    int createOrder(OrderDto orderDto);

    List<OrderDto> getOrderList();

    String deleteOrder(String orderUUID);

    String refundOrder(String orderUUID);
}
