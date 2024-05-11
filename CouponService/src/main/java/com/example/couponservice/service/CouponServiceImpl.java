package com.example.couponservice.service;

import com.example.couponservice.Entity.Coupon;
import com.example.couponservice.dto.CouponDto;
import com.example.couponservice.mapstruct.CouponMapStruct;
import com.example.couponservice.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;
    private final CouponMapStruct couponMapStruct;

    @Override
    @Transactional
    public int createCoupon(CouponDto couponDto) {
        Coupon coupon = couponMapStruct.changeEntity(couponDto);

        couponRepository.save(coupon);

        return 1;
    }
}
