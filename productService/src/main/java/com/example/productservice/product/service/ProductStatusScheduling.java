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
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductStatusScheduling {

    private final ProductRepository productRepository;

    @Transactional
    @Scheduled(cron = "0 */1 * * * *")
    public void changeStatus() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            if (product.getStatus() == Status.CLOSE) {
                LocalDateTime reservationTime = product.getReservationTime();
                System.out.println("reservationTime = " + reservationTime);
                System.out.println("now = " + now);

                if (reservationTime.isEqual(now)) {
                    product.changeStatus();
                    productRepository.save(product);
                }
            }
        }
    }
}
