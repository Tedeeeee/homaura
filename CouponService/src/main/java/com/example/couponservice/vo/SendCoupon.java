package com.example.couponservice.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendCoupon {
    private String couponUUID;
    private String memberUUID;
    private int discount;
}
