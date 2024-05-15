package com.example.userservice.member.service;

import com.example.userservice.member.entity.Member;
import com.example.userservice.member.entity.MemberCoupon;
import com.example.userservice.member.repository.MemberCouponRepository;
import com.example.userservice.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;

    @KafkaListener(topics = "coupon-topic")
    @Transactional
    public void insertMemberCoupon(String kafkaMessage) {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(map);

        String couponUUID = (String) map.get("couponUUID");
        String memberUUID = (String) map.get("memberUUID");
        int discount = (int) map.get("discount");

        Member member = memberRepository.findByMemberUUID(memberUUID);

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .couponUUID(couponUUID)
                .member(member)
                .discount(discount)
                .build();

        memberCouponRepository.save(memberCoupon);
        log.info("{} 님의 쿠폰 처리 완료", memberUUID);
    }
}
