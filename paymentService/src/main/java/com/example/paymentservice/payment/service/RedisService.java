package com.example.paymentservice.payment.service;

import java.util.Map;

public interface RedisService {

    // 주문
    Long getTotal(String key);
    String getValue(String key);
    void setValue(String key, String value);

    // 장바구니
    Map<String, String> getAllList(String key);
    void hSetValues(String key, String field, String value);
    void updateField(String key, String field, String increment);
    void deleteField(String key, String field);
    void deleteValue(String key);
}
