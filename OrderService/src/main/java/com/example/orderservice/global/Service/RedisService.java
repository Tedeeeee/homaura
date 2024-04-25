package com.example.orderservice.global.Service;

import java.time.Duration;
import java.util.Map;

public interface RedisService {
    void setValues(String key, String value);

    void setValues(String key, String value, Duration duration);

    void hsetValues(String key, String field, String value);

    String getValue(String key);

    void deleteValue(String key);

    void deleteField(String key, String field);
    void updateField(String key, String field, String increment);

    Map<String, String> getAllValues(String key);
}

