package com.example.userservice.member.service;

import com.example.userservice.member.dto.MemberDto;
import com.example.userservice.member.vo.RequestCheck;
import com.example.userservice.member.vo.RequestPassword;
import com.example.userservice.member.vo.ResponseCoupon;
import com.example.userservice.member.vo.ResponseMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface MemberService {
    String createMember(MemberDto memberDto, HttpSession session);
    String checkNickname(RequestCheck requestCheck);
    ResponseMember updateMember(MemberDto memberDto);
    String updatePassword(RequestPassword requestPassword,HttpServletRequest request);
    MemberDto getUser(HttpServletRequest request);

    ResponseCoupon getMyCoupon(String memberUUID);
}
