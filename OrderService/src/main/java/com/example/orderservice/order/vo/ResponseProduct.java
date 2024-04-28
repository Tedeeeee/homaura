package com.example.orderservice.order.vo;


import lombok.Data;

@Data
public class ResponseProduct {
    private String productUUID;
    private int stock;
    private int price;
    private long totalPrice;
}
