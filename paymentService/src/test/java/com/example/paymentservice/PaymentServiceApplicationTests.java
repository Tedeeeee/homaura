package com.example.paymentservice;

import com.example.paymentservice.payment.service.PaymentService;
import com.example.paymentservice.payment.vo.Content;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class PaymentServiceApplicationTests {

    @Autowired
    private PaymentService paymentService;

    private int idx;

    @Test
    void concurrencyTest() throws InterruptedException {
        int numThreads = 100;
        CountDownLatch donSignal = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                try {
                    paymentService.itemCount(newOrder(), null);
                    successCount.getAndIncrement();
                } catch (Exception e) {
                    failCount.getAndIncrement();
                } finally {
                    donSignal.countDown();
                }
            });
        }

        donSignal.await();
        executorService.shutdown();

        //then
        assertAll(
                () -> assertThat(successCount.get()).isEqualTo(100),
                () -> assertThat(failCount.get()).isEqualTo(0)
        );
    }

    private Content newOrder() {
        return new Content("ae14b92f-8d2a-4961-8546-b135183c799a");
    }
}
