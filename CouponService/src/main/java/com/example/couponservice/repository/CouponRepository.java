package com.example.couponservice.repository;

import com.example.couponservice.Entity.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Coupon c where c.couponUUID=:couponUUID")
    Coupon findByCouponUUIDForUpdate(String couponUUID);

    Coupon findByCouponUUID(String couponUUID);
}
