package com.example.userservice.member.service;

import com.example.userservice.member.client.CouponServiceClient;
import com.example.userservice.member.dto.MemberDto;
import com.example.userservice.member.entity.Member;
import com.example.userservice.member.entity.MemberCoupon;
import com.example.userservice.member.mapstruct.MemberMapStruct;
import com.example.userservice.member.repository.MemberCouponRepository;
import com.example.userservice.member.repository.MemberRepository;
import com.example.userservice.member.vo.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final MemberMapStruct memberMapStruct;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisService redisService;
    private final CouponServiceClient couponServiceClient;

    @Override
    public String checkNickname(RequestCheck requestCheck) {
        if (memberRepository.existsByNickname(requestCheck.getNickname())) {
            throw new RuntimeException("중복된 닉네임 입니다");
        }
        return "사용 가능한 닉네임입니다";
    }

    @Override
    public String createMember(MemberDto memberDto, HttpSession session) {
        String value = redisService.getValue(memberDto.getEmailCode());

        if (!value.equals(memberDto.getEmail()) || value.equals("")) {
            throw new RuntimeException("이메일 정보가 일치하지 않습니다. 다시 인증을 진행해주세요");
        }

        if (!memberDto.isNicknameVerified()) {
            throw new RuntimeException("닉네임 중복 확인을 해주세요");
        }
        String encodePwd = bCryptPasswordEncoder.encode(memberDto.getPassword());
        memberDto.setPassword(encodePwd);

        Member member = memberMapStruct.changeEntity(memberDto);

        try {
            memberRepository.save(member);
            session.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "회원 가입을 축하드립니다";
    }

    @Transactional
    @Override
    public ResponseMember updateMember(MemberDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.getEmail());

        if (member == null) {
            throw new RuntimeException("존재하지 않는 회원입니다");
        }
        member.changePhone(memberDto.getPhone());
        member.changeAddress(memberDto.getAddress());
        member.updateTime(LocalDateTime.now());

        MemberDto newMemberDto = memberMapStruct.changeMemberDto(member);

        return memberMapStruct.changeResponse(newMemberDto);
    }

    @Transactional
    @Override
    public String updatePassword(RequestPassword requestPassword, HttpServletRequest request) {
        String memberUUID = request.getHeader("uuid");
        Member member = memberRepository.findByMemberUUID(memberUUID);

        if (member == null) {
            throw new RuntimeException("존재하지 않는 회원입니다");
        }

        if (!bCryptPasswordEncoder.matches(requestPassword.getNowPassword(), member.getPassword())) {
            throw new RuntimeException("기존 비밀번호와 일치하지 않습니다");
        }

        if (!requestPassword.getNewPassword().equals(requestPassword.getRePassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        member.changePassword(bCryptPasswordEncoder.encode(requestPassword.getNewPassword()));
        member.updateTime(LocalDateTime.now());

        return "변경된 비밀번호로 다시 로그인해주세요";
    }

    @Override
    public MemberDto getUser(HttpServletRequest request) {
        String memberUUID = request.getHeader("uuid");
        Member member = memberRepository.findByMemberUUID(memberUUID);

        if (member == null) {
            throw new RuntimeException("존재하지 않는 회원입니다");
        }

        // 여기서 회원이 주문한 상품을 묶어서 전달해준다.
        return memberMapStruct.changeMemberDto(member);
    }

    @Override
    public ResponseCoupon getMyCoupon(String memberUUID) {
        Member member = memberRepository.findByMemberUUID(memberUUID);

        List<MemberCoupon> myCoupon = memberCouponRepository.findByMemberId(member.getId());

        List<ReceiveCoupon> list = new ArrayList<>();
        for (MemberCoupon memberCoupon : myCoupon) {
            Coupon coupon = couponServiceClient.findCoupon(memberCoupon.getCouponUUID());
            ReceiveCoupon receiveCoupon = new ReceiveCoupon(coupon.getName(), coupon.getDiscount());
            list.add(receiveCoupon);
        }

        return new ResponseCoupon(list.size(), list);
    }
}
