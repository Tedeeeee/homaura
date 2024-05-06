package com.example.productservice.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setValue(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    @Override
    public void setRemove(String key) {
        redisTemplate.opsForSet().remove(key);
    }

    @Override
    public Long countingCard(String key) {
        return redisTemplate.opsForSet().size(key);
    }


}
