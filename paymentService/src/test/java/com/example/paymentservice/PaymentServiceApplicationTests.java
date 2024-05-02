package com.example.paymentservice;

import com.example.paymentservice.payment.service.WishListService;
import com.example.paymentservice.payment.vo.AddWishListForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class PaymentServiceApplicationTests {

    @Autowired
    private WishListService wishListService;

    @Test
    void 선착순_10명_물건_구매하기() throws InterruptedException {
        int numThreads = 100;
        CountDownLatch doneSignal = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                try {
                    wishListService.putUniqueItem(newList(), null);
                    System.out.println(successCount.get() + "번째 성공");
                    successCount.getAndIncrement();
                } catch (Exception e) {
                    System.out.println(failCount.get() + "번째 실패");
                    failCount.getAndIncrement();
                } finally {
                    doneSignal.countDown();
                }
            });
        }

        doneSignal.await();
        executorService.shutdown();

        //then
        assertAll(
                () -> assertThat(successCount.get()).isEqualTo(10),
                () -> assertThat(failCount.get()).isEqualTo(90)
        );
    }

    private AddWishListForm newList() {
        return new AddWishListForm("47f7f65c-a968-437a-9515-b182a0642d98", "1");
    }
}
