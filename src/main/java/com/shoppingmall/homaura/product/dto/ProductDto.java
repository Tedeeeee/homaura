package com.shoppingmall.homaura.product.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String productUUID;
    private String name;
    private int price;
    private int stock;
    private String producer;
}
