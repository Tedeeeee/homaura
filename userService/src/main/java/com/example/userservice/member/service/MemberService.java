package com.example.userservice.member.service;

import com.example.userservice.member.dto.MemberDto;
import com.example.userservice.member.vo.RequestCheck;
import com.example.userservice.member.vo.RequestPassword;
import com.example.userservice.member.vo.ResponseMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface MemberService {
    String createMember(MemberDto memberDto, HttpSession session);
    String checkNickname(RequestCheck requestCheck);
    ResponseMember updateMember(MemberDto memberDto);
    String updatePassword(RequestPassword requestPassword,HttpServletRequest request);
    MemberDto getUser(HttpServletRequest request);
}
