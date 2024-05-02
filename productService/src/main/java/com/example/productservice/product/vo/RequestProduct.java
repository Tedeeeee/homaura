package com.example.productservice.product.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class RequestProduct {
    private String name;
    private int price;
    private int stock;
    private String producer;
    private String reservationTime;
}
