package com.shoppingmall.homaura.order.service;


import com.shoppingmall.homaura.order.dto.OrderDto;

public interface OrderService {
    String createOrder(OrderDto orderDto);
}
