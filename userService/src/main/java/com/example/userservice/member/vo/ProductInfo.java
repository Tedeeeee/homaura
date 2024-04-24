package com.example.userservice.member.vo;

import com.example.userservice.product.entity.Product;
import lombok.Data;

@Data
public class ProductInfo {
    private Product product;
    private int unitCount;
}
