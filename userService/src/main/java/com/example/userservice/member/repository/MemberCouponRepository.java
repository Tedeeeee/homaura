package com.example.userservice.member.repository;

import com.example.userservice.member.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    List<MemberCoupon> findByMemberId(long memberId);
}
