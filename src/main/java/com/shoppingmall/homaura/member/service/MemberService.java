package com.shoppingmall.homaura.member.service;

import com.shoppingmall.homaura.member.dto.MemberDto;
import com.shoppingmall.homaura.member.vo.RequestPassword;
import com.shoppingmall.homaura.member.vo.ResponseMember;
import jakarta.servlet.http.HttpSession;

public interface MemberService {
    String createMember(MemberDto memberDto, HttpSession session);
    String checkNickname(String nickname);
    ResponseMember updateMember(MemberDto memberDto);
    String updatePassword(RequestPassword requestPassword);
    String logout();

    MemberDto getUser();
}
