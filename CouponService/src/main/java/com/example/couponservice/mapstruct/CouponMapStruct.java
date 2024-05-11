package com.example.couponservice.mapstruct;

import com.example.couponservice.Entity.Coupon;
import com.example.couponservice.dto.CouponDto;
import com.example.couponservice.vo.RequestCoupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface CouponMapStruct {
    CouponDto changeDto(RequestCoupon requestCoupon);

    @Mapping(target = "couponUUID", expression = "java(java.util.UUID.randomUUID().toString())")
    Coupon changeEntity(CouponDto couponDto);
}
