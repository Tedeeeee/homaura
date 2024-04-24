package com.shoppingmall.homaura.member.repository;

import com.shoppingmall.homaura.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByMemberUUID(String memberUUID);

    RefreshToken findByRefreshToken(String refreshToken);
}
