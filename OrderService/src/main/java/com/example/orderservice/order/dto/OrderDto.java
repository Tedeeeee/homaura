package com.example.orderservice.order.dto;

import com.example.orderservice.order.entity.Content;
import com.example.orderservice.order.entity.Status;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private String orderUUID;
    private String memberUUID;
    private String deliveryAddress;
    private String deliveryPhone;
    private Long totalPrice;
    private Status status;
    private Status payment;
    private List<Content> products = new ArrayList<>();
}
