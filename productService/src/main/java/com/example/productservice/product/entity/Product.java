package com.example.productservice.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
