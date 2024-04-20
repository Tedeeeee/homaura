package com.shoppingmall.homaura.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productUUID;
    private String name;
    private int price;
    private int stock;
    private String producer;
    private LocalDateTime createAt;

    public void increaseStock(int count) {
        this.stock += count;
    }

    public void decreaseStock(int count) {
        this.stock -= count;
    }
}
