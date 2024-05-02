package com.example.productservice.product.dto;

import com.example.productservice.product.entity.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDto {
    private String productUUID;
    private String name;
    private int price;
    private int stock;
    private String producer;
    private String reservationTime;
    private Status status;
}
