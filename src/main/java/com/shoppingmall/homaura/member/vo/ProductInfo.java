package com.shoppingmall.homaura.member.vo;

import com.shoppingmall.homaura.product.entity.Product;
import lombok.Data;

@Data
public class ProductInfo {
    private Product product;
    private int unitCount;
}
