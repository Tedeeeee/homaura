package com.example.couponservice.service;

import com.example.couponservice.Entity.Coupon;
import com.example.couponservice.dto.CouponDto;
import com.example.couponservice.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;

    @Override
    @Transactional
    public int createCoupon(CouponDto couponDto) {

        Coupon coupon = Coupon.builder()
            .couponUUID(UUID.randomUUID().toString())
            .name(couponDto.getName())
            .discount(couponDto.getDiscount())
            .quantity(couponDto.getQuantity())
            .startTime(LocalDateTime.parse(couponDto.getStartTime()))
            .build();

        couponRepository.save(coupon);

        return 1;
    }

    @Override
    @Transactional
    public boolean decreaseStock(String couponUUID) {
        Coupon coupon = couponRepository.findByCouponUUIDForUpdate(couponUUID);

        if (coupon.getQuantity() == 0) {
            return false;
        }

        coupon.decreaseCount();
        return true;
    }


}
