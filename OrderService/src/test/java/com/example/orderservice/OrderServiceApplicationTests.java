package com.example.orderservice;

import com.example.orderservice.order.dto.OrderDto;
import com.example.orderservice.order.entity.Content;
import com.example.orderservice.order.entity.Status;
import com.example.orderservice.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
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
class OrderServiceApplicationTests {

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("재고보다 많은 주문이 동시에 들어오는 경우")
    void concurrencyOrder() throws InterruptedException {
        HttpServletRequest request = null;
        int numThreads = 2000;
        CountDownLatch doneSignal = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                try {
                    orderService.createOrder(newOrderDto(), null);
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
                () -> assertThat(successCount.get()).isEqualTo(2000),
                () -> assertThat(failCount.get()).isEqualTo(0)
        );
    }

    private OrderDto newOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setMemberUUID("7df75ec4-46ff-4200-a818-15eb662534b9");
        orderDto.setDeliveryAddress("서울 신촌 그 어딘가");
        orderDto.setDeliveryPhone("01098471261");
        orderDto.setStatus(Status.POSSIBLE);

        List<Content> contents = new ArrayList<>();
        Content content = new Content();
        content.setProductUUID("ae14b92f-8d2a-4961-8546-b135183c799a");
        content.setUnitCount(1);
        contents.add(content);

        orderDto.setProducts(contents);

        return orderDto;
    }
}
