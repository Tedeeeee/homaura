package com.example.productservice.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
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
    private LocalDateTime reservationTime;
    @Enumerated(value = EnumType.STRING)
    private Status status;

    public void increaseStock(int count) {
        this.stock += count;
    }
    public void decreaseStock(int count) { this.stock -= count; }

    public void changeStatus() {
        this.status = Status.OPEN;
    }
}
