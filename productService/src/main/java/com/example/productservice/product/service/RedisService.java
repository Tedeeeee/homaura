package com.example.productservice.product.service;


public interface RedisService {
    void hSetValues(String key, String field, String value);
    void deleteValue(String key);
}
