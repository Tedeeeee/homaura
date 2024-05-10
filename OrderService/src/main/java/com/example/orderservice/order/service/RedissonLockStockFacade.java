package com.example.orderservice.order.service;

import com.example.orderservice.order.entity.Content;
import com.example.orderservice.wishList.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RTransaction;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final RedisService redisService;

    public void increase(List<Content> contents) {
        RLock lock = redissonClient.getLock("stock");

        try {
            boolean available = lock.tryLock(10, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 획득 실패");
            }

            boolean flag = true;
            for (Content content : contents) {
                String productUUID = content.getProductUUID();
                int productCount = content.getUnitCount();

                int count = Integer.parseInt(redisService.getHashValue("product", productUUID));
                int value = Integer.parseInt(redisService.getValue("personal:" + productUUID));

                int stock = count - value;

                if (stock - productCount < 0) flag = false;

                redisService.increaseCount("personal:" + content.getProductUUID(), content.getUnitCount());
            }

            if (!flag) {
                redisService.decreaseCount(contents);
                throw new RuntimeException("재고 문제 발생");
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
