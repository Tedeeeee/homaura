package com.shoppingmall.homaura.member.repository;

import com.shoppingmall.homaura.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Member findByMemberUUID(String memberUUID);

    Member findByEmail(String email);
}
