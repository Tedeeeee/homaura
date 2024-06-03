package com.example.productservice.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String productUUID;
    private String name;
    private int price;
    private int stock;
    private String producer;
}
