package com.example.couponservice.service;

import com.example.couponservice.Entity.Coupon;
import com.example.couponservice.dto.CouponDto;

import java.util.List;

public interface CouponService {
    int createCoupon(CouponDto couponDto);
    boolean decreaseStock(String couponUUID);
}
