package com.example.paymentservice.global.Service;

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
    public String getValue(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) return "";
        return String.valueOf(values.get(key));
    }

    // 장바구니 관리
    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void deleteField(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    @Override
    public void hSetValues(String key, String field, String value) {
        HashOperations<String, String, String> values = redisTemplate.opsForHash();
        values.put(key, field, value);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    @Override
    public void updateField(String key, String field, String increment) {
        redisTemplate.opsForHash().increment(key, field, Long.parseLong(increment));
    }

    @Override
    public Map<String, String> getAllValues(String key) {
        return hashOperations.entries(key);
    }


    // 예약 상품 재고 관리
    @Override
    public void hSetUniqueValues(String key, String field, String value) {
        HashOperations<String, String, String> values = redisTemplate.opsForHash();
        values.put(key, field, value);
        redisTemplate.expire(key, 1, TimeUnit.MINUTES);
    }

    @Override
    public void decreaseValue(String key) {
        redisTemplate.opsForValue().increment(key, -1);
    }
}
