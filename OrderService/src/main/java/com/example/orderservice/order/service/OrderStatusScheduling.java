package com.example.orderservice.order.service;

import com.example.orderservice.order.client.ProductServiceClient;
import com.example.orderservice.order.entity.Content;
import com.example.orderservice.order.entity.Order;
import com.example.orderservice.order.entity.OrderProduct;
import com.example.orderservice.order.entity.Status;
import com.example.orderservice.order.repository.OrderProductRepository;
import com.example.orderservice.order.repository.OrderRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusScheduling {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductServiceClient productServiceClient;

    @Transactional
    @Scheduled(fixedRate = 10000, initialDelay = 3000)
    public void changeStatus() {
        List<Order> orderAll = orderRepository.findAll();

        for (Order order : orderAll) {
            // 0. DONE 이거나 CANCEL 상태면 더이상 상태변경 불가
            if (order.getStatus().equals(Status.DONE) || order.getStatus().equals(Status.CANCEL)) continue;

            // 1. createAt와 updateAt의 시간 차이 계산
            long createMinute = Duration.between(order.getCreateAt(), LocalDateTime.now()).toMinutes();
            long updateMinute = Duration.between(order.getUpdateAt(), LocalDateTime.now()).toMinutes();

            // 2. 반품이 된 상품인지 확인, 결제가 D-day
            if (!order.getStatus().equals(Status.REFUNDING)) {
                // D + 1 에 SHIPPING 으로 상태 변화
                if (createMinute >= 1) order.transferStatus(1);
                // D + 2 에 배달 완료
                if (createMinute >= 2) order.transferStatus(2);
                // D + 3 에 더 이상 상태 변경 불가
                if (createMinute >= 3) order.transferStatus(4);
            }

            // 3. 반품이 된 상품이라면 UpdateTime 으로 체크
            if (order.getStatus().equals(Status.REFUNDING)) {
                // 반품한지 1일이 지나면 취소 상태로 변경하고
                // 해당 주문에 묶인 물건의 재고를 원상 복구
                if (updateMinute >= 1) {
                    order.transferStatus(5);
                    updateProduct(order);
                }
            }

            orderRepository.save(order);
        }
    }

    public void updateProduct(Order order) {
        orderProductRepository.findByOrder(order).stream()
                .map(orderProduct -> new Content(orderProduct.getProductUUID(), orderProduct.getUnitCount()))
                .forEach(productServiceClient::increaseCount);
    }
}
