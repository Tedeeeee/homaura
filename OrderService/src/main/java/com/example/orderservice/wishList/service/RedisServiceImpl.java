package com.example.orderservice.wishList.service;

import com.example.orderservice.order.dto.OrderDto;
import com.example.orderservice.order.entity.Content;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.redisson.api.RLock;
import org.redisson.api.RTransaction;
import org.redisson.api.RedissonClient;
import org.redisson.api.TransactionOptions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService{

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, String> hashOperations;
    private final RedissonClient redissonClient;
    private final PlatformTransactionManager transactionManager;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate, PlatformTransactionManager transactionManager, RedissonClient redissonClient) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.redissonClient = redissonClient;
        this.transactionManager = transactionManager;
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
    public String getValue(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void increaseCount(String key, int value) {
        redisTemplate.opsForValue().increment(key, value);
    }

    public void decreaseCount(List<Content> contents) {

        for (Content content : contents) {
            redisTemplate.opsForValue().decrement("personal:" + content.getProductUUID(), content.getUnitCount());
        }
    }
}
