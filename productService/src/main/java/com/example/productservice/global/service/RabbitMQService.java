package com.example.productservice.global.service;

import com.example.productservice.product.entity.Content;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.repository.ProductRepository;
import com.example.productservice.product.vo.ResponseProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class RabbitMQService {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    private final ProductRepository productRepository;
    private final RedissonClient redissonClient;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void decreaseCount(Content content) {
        System.out.println("레디슨");
        RLock lock = redissonClient.getLock(content.getProductUUID());

        try{
            boolean available = lock.tryLock(5, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 획득 실패!");
            }
            Product product = productRepository.findByProductUUID(content.getProductUUID());

            if (product.getStock() < content.getUnitCount()) {
                return;
            }

            product.decreaseStock(content.getUnitCount());

            productRepository.save(product);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    @Transactional
//    public void decreaseProductCount(Content content) {
//        System.out.println("비관적");
//        Product product = productRepository.findByProductUUIDForUpdate(content.getProductUUID());
//
//        if (product == null) {
//            throw new IllegalArgumentException("제품을 찾을 수 없습니다");
//        }
//
//        if (product.getStock() < content.getUnitCount()) {
//            return;
//        }
//
//        product.decreaseStock(content.getUnitCount());
//
//        productRepository.save(product);
//    }
}
