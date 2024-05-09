package com.example.productservice.product.service;

import com.example.productservice.product.entity.Product;

public interface RedisService {
    void setKey(String key);
    void hSetValues(String key, String field, String value);
    void deleteField(String key, String field);
    void deleteValue(String key);
}
