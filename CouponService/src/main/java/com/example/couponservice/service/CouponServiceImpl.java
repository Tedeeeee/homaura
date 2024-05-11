package com.example.couponservice.service;

import com.example.couponservice.Entity.Coupon;
import com.example.couponservice.dto.CouponDto;
import com.example.couponservice.global.exception.BusinessExceptionHandler;
import com.example.couponservice.global.exception.ErrorCode;
import com.example.couponservice.mapstruct.CouponMapStruct;
import com.example.couponservice.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;
    private final CouponMapStruct couponMapStruct;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public int createCoupon(CouponDto couponDto) {
        Coupon coupon = couponMapStruct.changeEntity(couponDto);

        couponRepository.save(coupon);

        // set 의 데이터로 등록한 상품
        redisTemplate.opsForSet().add("event", coupon.getCouponUUID());
        return 1;
    }

    @Override
    @Transactional
    public void decreaseStock(String couponUUID) {
        Coupon coupon = couponRepository.findByCouponUUIDForUpdate(couponUUID);

        if (coupon.getQuantity() == 0) {
            throw new BusinessExceptionHandler("재고가 부족합니다",ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
        coupon.decreaseCount();
    }


}
