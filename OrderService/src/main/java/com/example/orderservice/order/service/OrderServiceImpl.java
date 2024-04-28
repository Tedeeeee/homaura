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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderMapStruct orderMapStruct;
    private final ProductServiceClient productServiceClient;

    @Override
    @Transactional
    public int createOrder(OrderDto orderDto, HttpServletRequest request) {
        // String uuid = request.getHeader("uuid");
        // 테스트 용
        String uuid = orderDto.getMemberUUID();

        Order order = Order.builder()
                .orderUUID(UUID.randomUUID().toString())
                .memberUUID(uuid)
                .deliveryAddress(orderDto.getDeliveryAddress())
                .deliveryPhone(orderDto.getDeliveryPhone())
                .status(Status.POSSIBLE)
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
}
