package com.example.orderservice.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderUUID;
    private String memberUUID;

    private String deliveryAddress;
    private String deliveryPhone;
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<OrderProduct> orderProductList = new ArrayList<>();

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void transferStatus(int day) {

        switch (day) {
            case 1 -> this.status = Status.SHIPPING;
            case 2 -> this.status = Status.REFUND;
            case 3 -> this.status = Status.REFUNDING;
            case 4 -> this.status = Status.DONE;
            case 5 -> this.status = Status.CANCEL;
        }

    }

    public void updateTime() {
        this.updateAt = LocalDateTime.now();
    }
}
