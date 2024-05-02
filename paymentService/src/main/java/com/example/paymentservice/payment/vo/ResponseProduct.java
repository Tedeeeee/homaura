package com.example.paymentservice.payment.vo;


import lombok.Data;

@Data
public class ResponseProduct {
    private String productUUID;
    private int stock;
    private int price;
    private long totalPrice;
}
