package com.example.productservice.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestProduct {
    private String name;
    private int price;
    private int stock;
    private String producer;
}
