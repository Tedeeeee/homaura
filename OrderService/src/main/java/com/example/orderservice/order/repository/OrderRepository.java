package com.example.orderservice.order.repository;

import com.example.orderservice.order.entity.Order;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByOrderUUID(String orderUUID);
    void deleteById(@NotNull Long orderId);

    @Query("select o from Order o join fetch o.orderProductList")
    List<Order> findByMemberUUID(String memberUUID);

}
