package com.example.couponservice.dto;

import lombok.Data;

@Data
public class CouponDto {
    private String name;
    private int discount;
    private int quantity;
    private String startTime;
}
