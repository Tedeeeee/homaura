package com.example.userservice.member.controller;

import com.example.userservice.member.entity.Member;
import com.example.userservice.member.entity.MemberCoupon;
import com.example.userservice.member.repository.MemberCouponRepository;
import com.example.userservice.member.repository.MemberRepository;
import com.example.userservice.member.vo.SendCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {

    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @PostMapping("")
    public void createMemberCoupon(@RequestBody SendCoupon sendCoupon) {
        try {
            Member member = memberRepository.findByMemberUUID(sendCoupon.getMemberUUID());

            MemberCoupon memberCoupon = MemberCoupon.builder()
                    .couponUUID(sendCoupon.getCouponUUID())
                    .discount(sendCoupon.getDiscount())
                    .member(member)
                    .build();

            memberCouponRepository.save(memberCoupon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
