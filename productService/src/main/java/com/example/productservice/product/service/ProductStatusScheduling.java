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
//@Component
@RequiredArgsConstructor
public class ProductStatusScheduling {

    private final ProductRepository productRepository;
    private final RedisService redisService;

    /**
     *  기획의 의도가 변경되면서 스케줄러를 사용하지 않게 됐다
     */

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
    @Scheduled(fixedRate = 6000000, initialDelay = 3000)
    public void cacheSchedule() {
        List<Product> products = productRepository.findAll();

        redisService.deleteValue("product");

        for (Product product : products) {
            if (product.getStatus().equals(Status.OPEN)) {
                redisService.hSetValues("product", product.getProductUUID(), String.valueOf(product.getStock()));
            }
        }
    }
}
