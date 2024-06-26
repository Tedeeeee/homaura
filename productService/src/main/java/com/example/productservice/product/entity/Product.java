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
    private String producer;
    private LocalDateTime createAt;
}
