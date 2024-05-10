package com.example.orderservice.wishList.service;

import com.example.orderservice.order.dto.OrderDto;
import com.example.orderservice.order.entity.Content;
import org.redisson.api.RLock;
import org.redisson.api.RTransaction;
import org.redisson.api.RLock;
import org.redisson.api.RTransaction;
import org.springframework.transaction.TransactionStatus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface RedisService {
    Map<String, String> getAllList(String key);
    void hSetValues(String key, String field, String value);
    void deleteField(String key, String field);
    void deleteValue(String key);

    // 재고 관련
    String getHashValue(String key, String field);
    String getValue(String key);
    void increaseCount(String key, int value);
    void decreaseCount(List<Content> contents);
}

