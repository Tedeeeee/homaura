package com.example.couponservice.controller;

import com.example.couponservice.dto.CouponDto;
import com.example.couponservice.service.CouponService;
import com.example.couponservice.service.EventService;
import com.example.couponservice.vo.RequestCoupon;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final EventService eventService;

    // 쿠폰 발행
    @PostMapping("")
    public ResponseEntity<Integer> createCoupon(@RequestBody RequestCoupon requestCoupon) {
        CouponDto couponDto = new CouponDto();
        couponDto.setName(requestCoupon.getName());
        couponDto.setDiscount(requestCoupon.getDiscount());
        couponDto.setQuantity(requestCoupon.getQuantity());
        couponDto.setStartTime(requestCoupon.getStartTime());
        return new ResponseEntity<>(couponService.createCoupon(couponDto), HttpStatus.CREATED);
    }

    // 선착순 대기 등록
    @GetMapping("")
    public void issuance(@RequestParam String couponUUID,
                         HttpServletRequest request) {
        //String memberUUID = request.getHeader("uuid");

        // test 용 memberUUID
        String memberUUID = UUID.randomUUID().toString();
        eventService.eventStart(couponUUID, memberUUID);
    }

    @GetMapping("/test")
    public void testControl() {
        eventService.enter();
    }
}
