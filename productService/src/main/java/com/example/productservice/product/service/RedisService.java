package com.example.productservice.product.service;

public interface RedisService {

    void setValue(String key, String value);

    void setRemove(String key);

    Long countingCard(String key);
}
