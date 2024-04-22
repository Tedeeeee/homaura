package com.shoppingmall.homaura.order.dto;

import com.shoppingmall.homaura.order.entity.Content;
import com.shoppingmall.homaura.order.entity.Status;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private String deliveryAddress;
    private String deliveryPhone;
    private Long totalPrice;
    private Status status;
    private List<Content> productUUIDs = new ArrayList<>();
}
