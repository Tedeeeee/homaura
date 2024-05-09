package com.example.productservice.product.service;

import com.example.productservice.product.entity.Product;
import com.example.productservice.product.entity.Status;
import com.example.productservice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductStatusScheduling {

    private final ProductRepository productRepository;
    private final RedisService redisService;
    private final ProductServiceImpl productService;

    @Transactional
    @Scheduled(cron = "0 */1 * * * *")
    public void changeStatus() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            if (product.getStatus() == Status.CLOSE) {
                LocalDateTime reservationTime = product.getReservationTime();

                if (reservationTime.isEqual(now)) {
                    product.changeStatus();
                    productRepository.save(product);
                }
            }
        }
    }

    @Transactional
    @Scheduled(fixedRate = 60000, initialDelay = 3000)
    public void cacheSchedule() {
        List<Product> products = productRepository.findAll();

        redisService.deleteValue("product");

        for (Product product : products) {
            if (product.getStatus().equals(Status.OPEN)) {
                redisService.setKey(product.getProductUUID());
                redisService.hSetValues("product", product.getProductUUID(), String.valueOf(product.getStock()));
            }
        }
    }
}
