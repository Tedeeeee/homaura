package com.shoppingmall.homaura.order.service;


import com.shoppingmall.homaura.order.dto.OrderDto;

import java.util.List;

public interface OrderService {
    int createOrder(OrderDto orderDto);

    List<OrderDto> getOrderList();

    String deleteOrder(String orderUUID);

    String refundOrder(String orderUUID);
}