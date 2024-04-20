package com.shoppingmall.homaura.order.dto;

import com.shoppingmall.homaura.order.entity.Content;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private String memberUUID;
    private List<Content> productUUIDs = new ArrayList<>();
    private String deliveryAddress;
    private String deliveryPhone;
}
