package com.example.couponservice.vo;

import lombok.Data;

@Data
public class RequestCoupon {
    private String name;
    private int discount;
    private int quantity;
}
