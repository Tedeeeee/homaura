package com.example.productservice.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productStock")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productUUID;
    private int stock;

    public void increaseStock(int count) {
        this.stock += count;
    }
    public void decreaseStock(int count) { this.stock -= count; }
}
