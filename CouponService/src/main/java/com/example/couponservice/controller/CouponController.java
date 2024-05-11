package com.example.couponservice.controller;

import com.example.couponservice.dto.CouponDto;
import com.example.couponservice.mapstruct.CouponMapStruct;
import com.example.couponservice.service.CouponService;
import com.example.couponservice.service.EventService;
import com.example.couponservice.vo.RequestCoupon;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final CouponMapStruct couponMapStruct;
    private final EventService eventService;

    // 쿠폰 발행
    @PostMapping("")
    public ResponseEntity<Integer> createCoupon(@RequestBody RequestCoupon requestCoupon) {
        CouponDto couponDto = couponMapStruct.changeDto(requestCoupon);
        return new ResponseEntity<>(couponService.createCoupon(couponDto), HttpStatus.CREATED);
    }

    // 선착순 대기 등록
    @GetMapping("")
    public void issuance(@RequestParam String couponUUID, HttpServletRequest request) {
        String MemberUUID = request.getHeader("uuid");
        eventService.eventStart(couponUUID, MemberUUID);
    }
}
