package com.example.orderservice.wishList.service;

import java.util.Map;

public interface RedisService {
    Map<String, String> getAllList(String key);
    void hSetValues(String key, String field, String value);
    void deleteField(String key, String field);
    void deleteValue(String key);

}

