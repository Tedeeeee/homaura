package com.example.userservice.member.vo;

import lombok.Data;

@Data
public class SendCoupon {
    private String couponUUID;
    private int discount;
    private String memberUUID;
}
