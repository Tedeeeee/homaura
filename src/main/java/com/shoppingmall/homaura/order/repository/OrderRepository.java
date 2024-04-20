package com.shoppingmall.homaura.order.repository;

import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.order.entity.Order;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByMember(Member member);

    Order findByOrderUUID(String orderUUID);

    void deleteById(Long orderId);

}
