package com.shoppingmall.homaura.order.repository;

import com.shoppingmall.homaura.order.entity.Order;
import com.shoppingmall.homaura.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByOrder(Order order);
}
