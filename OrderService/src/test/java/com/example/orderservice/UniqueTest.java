package com.example.orderservice;

import com.example.orderservice.order.client.ProductServiceClient;
import com.example.orderservice.order.dto.OrderDto;
import com.example.orderservice.order.entity.Content;
import com.example.orderservice.order.service.OrderService;
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
public class UniqueTest {

    @Autowired
    private ProductServiceClient productServiceClient;
    @Autowired
    private OrderService orderService;

    @Test
    void 선착순_10명_물건_구매하기() throws InterruptedException {
        int numThreads = 50;
        CountDownLatch doneSignal = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        AtomicInteger userName = new AtomicInteger(1);

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                try {
                    String user = (userName.getAndIncrement()) + "번 유저";
                    //상품 누르기
                    productServiceClient.decreaseCount(new Content("47f7f65c-a968-437a-9515-b182a0642d98", 1));

                    // 여기서 20%가 이탈 -> 이탈하면 재고 증가


                    // 실행시 order 생성
                    OrderDto uniqueOrder = orderService.createUniqueOrder(newOrderDto(user), null);

                    // 여기서 20%가 실패 -> 실패하면 재고 증가

                    // 결제 성공
                    orderService.changePayment(uniqueOrder.getOrderUUID());


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
                () -> assertThat(successCount.get()).isEqualTo(50),
                () -> assertThat(failCount.get()).isEqualTo(0)
        );
    }

    private OrderDto newOrderDto(String user) {
        OrderDto orderDto = new OrderDto();
        orderDto.setMemberUUID(user);
        orderDto.setDeliveryAddress("서울 신촌 그 어딘가");
        orderDto.setDeliveryPhone("01098471261");

        List<Content> contents = new ArrayList<>();
        Content content = new Content();
        content.setProductUUID("47f7f65c-a968-437a-9515-b182a0642d98");
        content.setUnitCount(1);
        contents.add(content);

        orderDto.setProducts(contents);

        return orderDto;
    }
}
