package com.example.orderservice.wishList.service;


import com.example.orderservice.order.entity.Content;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
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
    public void deleteField(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }
    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
    public String getHashValue(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }
    @Override
    public void decreaseCount(Content content) {
        redisTemplate.opsForHash().increment("product", content.getProductUUID(), -(content.getUnitCount()));
    }
    @Override
    public void increaseCount(List<Content> contents) {
        for (Content content : contents) {
            redisTemplate.opsForHash().increment("product", content.getProductUUID(), content.getUnitCount());
        }
    }
}
