package com.example.productservice.product.service;

import com.example.productservice.product.entity.Content;

public interface ProductStockService {
    void increaseCount(Content content);

    void decreaseCount(Content content);

    boolean checkStock(String productUUID, int unitCount);
}
