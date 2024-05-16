package com.example.orderservice.order.service;

import com.example.orderservice.global.exception.BusinessExceptionHandler;
import com.example.orderservice.global.exception.ErrorCode;
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

import java.util.ArrayList;
import java.util.List;

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
    @Retry(name = "retry", fallbackMethod = "retryFallback")
    @CircuitBreaker(name = "breaker", fallbackMethod = "fallback")
    public String createOrders(OrderDto orderDto) {
        productServiceClient.decreaseCount(orderDto.getProducts());

        Order order = orderMapStruct.changeEntity(orderDto);

        createOrderProduct(orderDto, order);

        orderRepository.save(order);

        return order.getOrderUUID();
    }

    public int retryFallback(OrderDto orderDto, HttpServletRequest request, Throwable t) {
        log.error("retry : " + t.getMessage());
        throw new BusinessExceptionHandler("상품 주문 실패", ErrorCode.BUSINESS_EXCEPTION_ERROR);
    }

    public int fallback(OrderDto orderDto, HttpServletRequest request, Throwable t) {
        log.error("FallBack : " + t.getMessage());
        throw new BusinessExceptionHandler("상품 주문 실패", ErrorCode.BUSINESS_EXCEPTION_ERROR);
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
            throw new BusinessExceptionHandler("존재하지 않는 주문입니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        if (!order.getStatus().equals(Status.POSSIBLE)) {
            throw new BusinessExceptionHandler("주문을 취소 할 수 있는 날짜를 지났습니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        try {
            List<OrderProduct> orderProductList = order.getOrderProductList();

            for (OrderProduct orderProduct : orderProductList) {
                String productUUID = orderProduct.getProductUUID();

                // 각각의 상품의 주문 갯수 만큼 다시 product에 넣어주기
                productServiceClient.increaseCount(new Content(productUUID, orderProduct.getUnitCount()));
            }

            orderRepository.deleteById(order.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessExceptionHandler(e.getMessage(), ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        return "주문이 취소 되었습니다";
    }

    @Override
    public String refundOrder(String orderUUID) {
        Order order = orderRepository.findByOrderUUID(orderUUID);

        if (!order.getStatus().equals(Status.REFUND)) {
            throw new BusinessExceptionHandler("반품 기간이 아닙니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
        order.transferStatus(3);
        order.updateTime();

        orderRepository.save(order);
        return "반품이 진행됩니다";
    }

    private void createOrderProduct(OrderDto orderDto, Order order) {
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
}
