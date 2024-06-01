package com.example.couponservice.service;

import com.example.couponservice.Entity.Coupon;
import com.example.couponservice.repository.CouponRepository;
import com.example.couponservice.vo.SendCoupon;
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
    private final KafkaProducerService kafkaProducerService;

    // 이벤트는 무조건 한번에 한개의 이벤트가 끝나기 전까지 다른 이벤트가 발생하지 않는다.
    public void eventStart(String couponUUID, String memberUUID) {
        long now = System.currentTimeMillis();
        // 참가열에 넣는 시도를 한다
        redisTemplate.opsForZSet().add(couponUUID, memberUUID, now);

        log.info("쿠폰 명 : {} , {} 님 진입 완료!, 쓰레드 명: {} ({}초)", couponUUID, memberUUID, Thread.currentThread().getName(), now);
    }

    // 참가열에 있는 사용자중 10명을 처리하고
    // 대기열에서 참가열로 10명을 넣기
    public boolean enter() {
        String couponUUID = redisTemplate.opsForValue().get("event");

        if (couponUUID == null) return false;

        Set<String> participationQueue = redisTemplate.opsForZSet().range(couponUUID, 0, -1);

        System.out.println("participationQueue = " + participationQueue);

        if (isEnd(couponUUID)) return false;

        for (String memberUUID : participationQueue) {
            if (isEnd(couponUUID)) {
                return false;
            }

            // 쿠폰 잔여 갯수 내리기
            if (!couponService.decreaseStock(couponUUID)) break;
            redisTemplate.opsForZSet().remove(couponUUID, memberUUID);

            Coupon coupon = couponRepository.findByCouponUUID(couponUUID);

            SendCoupon sendCoupon = SendCoupon.builder()
                    .couponUUID(couponUUID)
                    .memberUUID(memberUUID)
                    .discount(coupon.getDiscount())
                    .build();

            // kafka 사용
            kafkaProducerService.send("coupon-topic", sendCoupon);

            log.info("{}님의 응답 제출이 완료되었습니다. 쿠폰 : {}", memberUUID, couponUUID);
        }
        return true;
    }

    // 대기열 순번 안내
    public boolean getOrder() {
        String couponUUID = redisTemplate.opsForValue().get("event");

        if (couponUUID == null) return false;
        if (isEnd(couponUUID)) return false;

        Set<String> waitingQueue = redisTemplate.opsForZSet().range(couponUUID, 0, 9);

        for (String memberUUID : waitingQueue) {
            Long rank = redisTemplate.opsForZSet().rank(couponUUID, memberUUID);
            log.info("{} 님의 현재 대기번호는 {}입니다", memberUUID, rank + 1);
        }
        log.info("남은 인원 수 : {}", getWaitingQueueSize(couponUUID));

        return true;
    }

    private boolean isEnd(String couponUUID) {
        Coupon coupon = couponRepository.findByCouponUUID(couponUUID);

        if (coupon == null) return false;

        if (coupon.getQuantity() == 0) {
            Set<String> waitingPeople = redisTemplate.opsForZSet().range(couponUUID, 0, -1);

            if (waitingPeople == null) {
                return true;
            }

            for (String waitingPerson : waitingPeople) {
                log.info("{}님에게 마감 소식 알림", waitingPerson);
                redisTemplate.opsForZSet().remove(couponUUID, waitingPerson);
            }

            endEvent();
            return true;
        }
        return false;
    }

    public Long getWaitingQueueSize(String couponUUID) {
        return redisTemplate.opsForZSet().size(couponUUID);
    }

    private void endEvent() {
        redisTemplate.expire("event", 3, TimeUnit.MINUTES);
    }
}
