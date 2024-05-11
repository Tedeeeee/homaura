package com.example.orderservice.wishList.service;

import com.example.orderservice.order.entity.Content;

import java.util.List;
import java.util.Map;

public interface RedisService {
    Map<String, String> getAllList(String key);
    void hSetValues(String key, String field, String value);
    void deleteField(String key, String field);
    void deleteValue(String key);

    // 재고 관련
    String getHashValue(String key, String field);
    void decreaseCount(Content content);
    void increaseCount(List<Content> contents);
}

