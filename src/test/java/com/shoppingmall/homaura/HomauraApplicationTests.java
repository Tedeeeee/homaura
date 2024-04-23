package com.shoppingmall.homaura;

import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.member.entity.Role;
import com.shoppingmall.homaura.member.repository.MemberRepository;
import com.shoppingmall.homaura.order.dto.OrderDto;
import com.shoppingmall.homaura.order.entity.Content;
import com.shoppingmall.homaura.order.service.OrderService;
import com.shoppingmall.homaura.product.entity.Product;
import com.shoppingmall.homaura.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class HomauraApplicationTests {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("재고보다 많은 주문이 동시에 들어오는 경우")
    void concurrencyOrder() throws InterruptedException {
        // given
        // 물품
        Product product = new Product(1L, "e03752e0-1c38-42f6-9eee-162df341318d", "예감", 1000, 100, "윤태식", LocalDateTime.now());
        productRepository.save(product);

        // 사용자
        Member member =
                Member.builder()
                        .email("gksmf359@gmail.com")
                        .password("!asdfasdF")
                        .name("윤태식")
                        .nickname("dkahffk")
                        .phone("01012341234")
                        .address("나도 몰라")
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .memberUUID("b9f5ba4e-0e26-4a9b-8cca-923d7d5d796b")
                        .role(Role.USER)
                        .build();

        memberRepository.save(member);

        int numThreads = 100;
        CountDownLatch doneSignal = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                try {
                    orderService.createOrder(createOrderDto());
                    System.out.println("성공");
                    successCount.getAndIncrement();
                } catch (Exception e) {
                    System.out.println("실패");
                    failCount.getAndIncrement();
                } finally {
                    doneSignal.countDown();
                }
            });
        }

        doneSignal.await();
        executorService.shutdown();
        Product product1 = productRepository.findByProductUUID(product.getProductUUID());
        System.out.println(product1.getStock());

        //then
        assertAll(
                () -> assertThat(successCount.get()).isEqualTo(5),
                () -> assertThat(failCount.get()).isEqualTo(5)
        );
    }

    private OrderDto createOrderDto() {
        OrderDto orderDto = new OrderDto();
        List<Content> orderContents = new ArrayList<>();
        Content content = new Content();
        content.setProductUUID("e03752e0-1c38-42f6-9eee-162df341318d");
        content.setUnitCount(2);
        orderContents.add(content);

        orderDto.setMemberUUID("b9f5ba4e-0e26-4a9b-8cca-923d7d5d796b");
        orderDto.setProductUUIDs(orderContents);
        orderDto.setDeliveryAddress("서울 신촌로 그 어딘가");
        orderDto.setDeliveryPhone("01012341234");

        return orderDto;
    }
}
