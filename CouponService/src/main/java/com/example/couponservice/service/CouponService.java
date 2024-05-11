package com.example.couponservice.service;

import com.example.couponservice.dto.CouponDto;

public interface CouponService {
    int createCoupon(CouponDto couponDto);
    void decreaseStock(String couponUUID);

}
