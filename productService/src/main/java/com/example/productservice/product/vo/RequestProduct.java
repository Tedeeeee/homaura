package com.example.productservice.product.vo;

import lombok.Data;

@Data
public class RequestProduct {
    private String name;
    private int price;
    private int stock;
    private String producer;
    private String reservationTime;
}
