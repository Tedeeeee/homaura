package com.shoppingmall.homaura.member.service;

import com.shoppingmall.homaura.member.dto.MemberDto;
import jakarta.servlet.http.HttpSession;

public interface MemberService {
    String createMember(MemberDto memberDto, HttpSession session);

    String checkNickname(String nickname);

}
