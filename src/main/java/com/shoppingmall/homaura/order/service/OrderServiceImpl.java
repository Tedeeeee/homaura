package com.shoppingmall.homaura.order.service;

import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.member.repository.MemberRepository;
import com.shoppingmall.homaura.order.dto.OrderDto;
import com.shoppingmall.homaura.order.entity.Content;
import com.shoppingmall.homaura.order.entity.Order;
import com.shoppingmall.homaura.order.entity.OrderProduct;
import com.shoppingmall.homaura.order.entity.Status;
import com.shoppingmall.homaura.order.repository.OrderProductRepository;
import com.shoppingmall.homaura.order.repository.OrderRepository;
import com.shoppingmall.homaura.product.entity.Product;
import com.shoppingmall.homaura.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @Override
    @Transactional
    public String createOrder(OrderDto orderDto) {
        Member member = memberRepository.findByMemberUUID(orderDto.getMemberUUID());

        if (member == null) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다");
        }

        try {
            Order order = Order.builder()
                    .orderUUID(UUID.randomUUID().toString())
                    .member(member)
                    .deliveryAddress(orderDto.getDeliveryAddress())
                    .deliveryPhone(orderDto.getDeliveryPhone())
                    .isRefund(false)
                    .status(Status.POSSIBLE)
                    .createAt(LocalDateTime.now())
                    .build();

            order.setTotalPrice(getTotalPrice(orderDto));

            orderRepository.save(order);

            for (Content content : orderDto.getProductUUIDs()) {
                Product product = productRepository.findByProductUUID(content.getProductUUID());

                if (product == null) {
                    throw new IllegalArgumentException("제품을 찾을 수 없습니다");
                }

                if (product.getStock() < content.getUnitCount()) {
                    throw new RuntimeException("재고가 부족하여 주문을 생성할 수 없습니다");
                }

                product.decreaseStock(content.getUnitCount());

                productRepository.save(product);

                OrderProduct orderProduct = OrderProduct.builder()
                        .order(order)
                        .product(product)
                        .unitCount(content.getUnitCount())
                        .build();
                orderProductRepository.save(orderProduct);
            }

            return "주문을 성공하였습니다";
        } catch (Exception e) {
            e.printStackTrace();
            return "주문에 실패하였습니다";
        }
    }

    private Long getTotalPrice(OrderDto orderDto) {
        long totalPrice = 0L;
        for (Content content : orderDto.getProductUUIDs()) {
            Product product = productRepository.findByProductUUID(content.getProductUUID());
            totalPrice += (long) product.getPrice() * content.getUnitCount();
        }
        return totalPrice;
    }
}
