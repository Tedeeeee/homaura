package com.example.paymentservice.global.Service;

import java.time.Duration;
import java.util.Map;

public interface RedisService {

    void hSetValues(String key, String field, String value);
    String getValue(String key);
    void deleteValue(String key);
    void deleteField(String key, String field);
    void updateField(String key, String field, String increment);
    Map<String, String> getAllValues(String key);

    // 예약 상품 관리
    void hSetUniqueValues(String key, String field, String value);
    void decreaseValue(String key);
}

