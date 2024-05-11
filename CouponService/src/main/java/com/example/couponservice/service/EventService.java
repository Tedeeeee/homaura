package com.example.couponservice.service;

import com.example.couponservice.Entity.Coupon;
import com.example.couponservice.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final RedisTemplate<String, String> redisTemplate;
    private final CouponService couponService;
    private final CouponRepository couponRepository;

    // 이벤트는 무조건 한번에 한개의 이벤트가 끝나기 전까지 다른 이벤트가 발생하지 않는다.
    public void eventStart(String couponUUID, String memberUUID) {
        long now = System.currentTimeMillis();
        // 참가열에 넣는 시도를 한다
        redisTemplate.opsForZSet().add(couponUUID, memberUUID, now);

        log.info("쿠폰 명 : {} , {} 님 진입 완료!, 쓰레드 명: {} ({}초)", couponUUID, memberUUID, Thread.currentThread().getName(), now);
    }

    // 참가열에 있는 사용자중 10명을 처리하고
    // 대기열에서 참가열로 10명을 넣기
    public void enter() {
        String couponUUID = redisTemplate.opsForValue().get("event");

        if (couponUUID == null) return;

        Set<String> participationQueue = redisTemplate.opsForZSet().range(couponUUID, 0, 10);

        if (participationQueue == null) {
            return;
        }

        for (String memberUUID : participationQueue) {
            if (isEnd(couponUUID)) {
                endEvent();
                return;
            }

            // 쿠폰 잔여 갯수 내리기
            couponService.decreaseStock(couponUUID);
            redisTemplate.opsForZSet().remove(couponUUID, memberUUID);
            // Kafka를 통해 사용자의 쿠폰 테이블 데이터 넣기

            log.info("{}님의 응답 제출이 완료되었습니다. 쿠폰 : {}", memberUUID, couponUUID);
        }
    }

    // 대기열 순번 안내
    public void getOrder() {
        String couponUUID = redisTemplate.opsForValue().get("event");

        if (couponUUID == null) return;
        if (isEnd(couponUUID)) return;

        Set<String> waitingQueue = redisTemplate.opsForZSet().range(couponUUID, 0, -1);

        if (waitingQueue == null) return;

        for (String memberUUID : waitingQueue) {
            Long rank = redisTemplate.opsForZSet().rank(couponUUID, memberUUID);
            log.info("{} 님의 현재 대기번호는 {}입니다", memberUUID, rank);
        }
    }

    private boolean isEnd(String couponUUID) {
        Coupon coupon = couponRepository.findByCouponUUID(couponUUID);

        if (coupon.getQuantity() == 0) {
            Set<String> waitingPeople = redisTemplate.opsForZSet().range(couponUUID, 0, -1);

            if (waitingPeople == null) {
                return true;
            }

            for (Object waitingPerson : waitingPeople) {
                log.info("{}님에게 마감 소식 알림", waitingPerson);
            }
            return true;
        }
        return false;
    }
    private void endEvent() {
        redisTemplate.expire("event", 30, TimeUnit.MINUTES);
    }
}
