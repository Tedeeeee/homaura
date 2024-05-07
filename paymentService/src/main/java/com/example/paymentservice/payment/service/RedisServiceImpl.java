package com.example.paymentservice.payment.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService{

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, String> hashOperations;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Long getTotal(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public String getValue(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) return "0";
        return String.valueOf(values.get(key));
    }

    @Override
    public void setValue(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }


    // 장바구니
    @Override
    public Map<String, String> getAllList(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public void hSetValues(String key, String field, String value) {
        HashOperations<String, String, String> values = redisTemplate.opsForHash();
        values.put(key, field, value);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
    }

    @Override
    public void updateField(String key, String field, String increment) {
        redisTemplate.opsForHash().increment(key, field, Long.parseLong(increment));
    }

    @Override
    public void deleteField(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}
