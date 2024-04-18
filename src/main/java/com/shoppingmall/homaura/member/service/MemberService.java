package com.shoppingmall.homaura.member.service;

import com.shoppingmall.homaura.member.dto.MemberDto;

public interface MemberService {
    String createMember(MemberDto memberDto);

    String checkNickname(String nickname);
}
