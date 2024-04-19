package com.shoppingmall.homaura.member.repository;

import com.shoppingmall.homaura.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByMemberUUID(String memberUUID);

}
