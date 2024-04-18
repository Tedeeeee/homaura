package com.shoppingmall.homaura.member.service;

import com.shoppingmall.homaura.member.dto.MemberDto;
import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.member.mapstruct.MemberMapStruct;
import com.shoppingmall.homaura.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapStruct memberMapStruct;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new RuntimeException("중복된 닉네임 입니다");
        }
        return "사용 가능한 닉네임입니다";
    }

    @Override
    public String createMember(MemberDto memberDto, HttpSession session) {
        String findEmail = (String) session.getAttribute(memberDto.getEmailCode());

        if (!findEmail.equals(memberDto.getEmail()) || findEmail.equals("")) {
            throw new RuntimeException("이메일 인증을 진행해주세요");
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
}
