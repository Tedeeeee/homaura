package com.example.orderservice.order.dto;

import com.example.orderservice.order.entity.Content;
import com.example.orderservice.order.entity.Status;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private String memberUUID;
    private String deliveryAddress;
    private String deliveryPhone;
    private Long totalPrice;
    private Status status;
    private List<Content> productUUIDs = new ArrayList<>();
}
