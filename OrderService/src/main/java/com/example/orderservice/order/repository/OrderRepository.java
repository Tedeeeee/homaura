package com.example.orderservice.order.repository;

import com.example.orderservice.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    Order findByOrderUUID(String orderUUID);

    void deleteById(Long orderId);

}
