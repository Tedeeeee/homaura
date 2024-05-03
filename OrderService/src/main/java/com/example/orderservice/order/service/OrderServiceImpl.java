package com.example.orderservice.order.service;

import com.example.orderservice.order.client.ProductServiceClient;
import com.example.orderservice.order.vo.ResponseProduct;
import com.example.orderservice.order.dto.OrderDto;
import com.example.orderservice.order.entity.Content;
import com.example.orderservice.order.entity.Order;
import com.example.orderservice.order.entity.OrderProduct;
import com.example.orderservice.order.entity.Status;
import com.example.orderservice.order.mapstruct.OrderMapStruct;
import com.example.orderservice.order.repository.OrderProductRepository;
import com.example.orderservice.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderMapStruct orderMapStruct;
    private final ProductServiceClient productServiceClient;

    @Override
    @Transactional
    //@Retry(name = "retry", fallbackMethod = "retryFallback")
    //@CircuitBreaker(name = "breaker", fallbackMethod = "fallback")
    public int createOrder(OrderDto orderDto, HttpServletRequest request) {
        String uuid = request.getHeader("uuid");
        // 테스트 용
        //String uuid = orderDto.getMemberUUID();

        Order order = Order.builder()
                .orderUUID(UUID.randomUUID().toString())
                .memberUUID(uuid)
                .deliveryAddress(orderDto.getDeliveryAddress())
                .deliveryPhone(orderDto.getDeliveryPhone())
                .payment(Status.READY)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        long totalPrice = 0L;
        for (Content content : orderDto.getProducts()) {
            ResponseProduct product = productServiceClient.decreaseCount(content);

            OrderProduct orderProduct = OrderProduct.builder()
                    .order(order)
                    .productUUID(content.getProductUUID())
                    .unitCount(content.getUnitCount())
                    .build();
            orderProductRepository.save(orderProduct);

            totalPrice += (long) product.getPrice() * content.getUnitCount();
        }
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);

        return 1;
    }

    private int fallback(OrderDto orderDto, HttpServletRequest request, Throwable t) {
        log.info("FallBack 이 사용되었다.");
        log.error("FallBack : " + t.getMessage());
        throw new RuntimeException("상품 주문 실패");
    }

    private int retryFallback(OrderDto orderDto, HttpServletRequest request, Throwable t) {
        log.info("retry 가 사용되었다.");
        log.error("retry : " + t.getMessage());
        throw new RuntimeException("상품 주문 실패");
    }

    @Override
    public List<OrderDto> getOrderList(HttpServletRequest request) {
        String uuid = request.getHeader("uuid");

        List<Order> orders = orderRepository.findByMemberUUID(uuid);
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (Order order : orders) {
            orderDtoList.add(convertToDto(order));
        }

        return orderDtoList;
    }

    @Override
    @Transactional
    public String deleteOrder(String orderUUID) {
        Order order = orderRepository.findByOrderUUID(orderUUID);

        if (order == null) {
            throw new RuntimeException("존재하지 않는 주문입니다");
        }

        if (!order.getStatus().equals(Status.POSSIBLE)) {
            throw new RuntimeException("주문을 취소 할 수 있는 날짜를 지났습니다");
        }

        try {
            List<OrderProduct> orderProductList = order.getOrderProductList();

            for (OrderProduct orderProduct : orderProductList) {
                // 각각의 상품의 주문 갯수 만큼 다시 product 테이블
                String productUUID = orderProduct.getProductUUID();

                productServiceClient.increaseCount(new Content(productUUID, orderProduct.getUnitCount()));
            }

            orderRepository.deleteById(order.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("주문실패");
        }

        return "주문이 취소 되었습니다";
    }

    @Override
    public String refundOrder(String orderUUID) {
        Order order = orderRepository.findByOrderUUID(orderUUID);

        if (!order.getStatus().equals(Status.REFUND)) {
            throw new RuntimeException("반품 기간이 아닙니다");
        }
        order.transferStatus(3);
        order.updateTime();

        orderRepository.save(order);
        return "반품이 진행됩니다";
    }

    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = orderMapStruct.changeDto(order);
        List<Content> contents = new ArrayList<>();
        for (OrderProduct orderProduct : order.getOrderProductList()) {
            Content content = new Content();
            content.setProductUUID(orderProduct.getProductUUID());
            content.setUnitCount(orderProduct.getUnitCount());
            contents.add(content);
        }
        orderDto.setProducts(contents);
        return orderDto;
    }

    // 예약 구매 서비스
    @Override
    @Transactional
    public OrderDto createUniqueOrder(OrderDto orderDto, HttpServletRequest request) {
        String uuid = request.getHeader("uuid");
        // 테스트 용
        //String uuid = orderDto.getMemberUUID();

        Order order = Order.builder()
                .orderUUID(UUID.randomUUID().toString())
                .memberUUID(uuid)
                .deliveryAddress(orderDto.getDeliveryAddress())
                .deliveryPhone(orderDto.getDeliveryPhone())
                .payment(Status.READY)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        long totalPrice = 0L;
        for (Content content : orderDto.getProducts()) {
            ResponseProduct product = productServiceClient.existProduct(content.getProductUUID());

            OrderProduct orderProduct = OrderProduct.builder()
                    .order(order)
                    .productUUID(content.getProductUUID())
                    .unitCount(content.getUnitCount())
                    .build();
            orderProductRepository.save(orderProduct);

            totalPrice += (long) product.getPrice() * content.getUnitCount();
        }
        order.setTotalPrice(totalPrice);

        OrderDto ordDto = orderMapStruct.changeDto(order);
        ordDto.setProducts(orderDto.getProducts());

        return ordDto;
    }

    @Override
    @Transactional
    public int changePayment(String orderUUID) {
        Order order = orderRepository.findByOrderUUID(orderUUID);
        order.changePaymentStatus();
        return 1;
    }
}
