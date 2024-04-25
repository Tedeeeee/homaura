package com.example.orderservice.order.entity;

import com.shoppingmall.homaura.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders_product")
@Builder
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderUUID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productUUID")
    private Product product;
    private int unitCount;
}
