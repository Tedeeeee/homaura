package com.shoppingmall.homaura.order.service;


import com.shoppingmall.homaura.order.dto.OrderDto;

import java.util.List;

public interface OrderService {
    String createOrder(OrderDto orderDto);

    List<OrderDto> getOrderList(String memberUUID);

    String deleteOrder(String orderUUID);
}
