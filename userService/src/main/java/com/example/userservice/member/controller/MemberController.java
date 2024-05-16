package com.example.userservice.member.controller;

import com.example.userservice.member.dto.MemberDto;
import com.example.userservice.member.mapstruct.MemberMapStruct;
import com.example.userservice.member.service.MailService;
import com.example.userservice.member.service.MemberService;
import com.example.userservice.member.vo.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MemberController {

    private final MemberMapStruct memberMapStruct;
    private final MailService mailService;
    private final MemberService memberService;

    // 이메일 인증 ( 중복된 이메일이 있을때 가입 불가능 )
    @PostMapping("/validationEmail")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody RequestCheck requestCheck) {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.sendEmail(requestCheck));
    }

    // 인증 코드 확인
    @PostMapping("/checkCode")
    public ResponseEntity<String> checkCode(@Valid @RequestBody RequestCheck requestCheck) {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.checkCode(requestCheck));
    }

    // 닉네임 중복 확인
    @PostMapping("/checkNickname")
    public ResponseEntity<String> checkNickname(@Valid @RequestBody RequestCheck requestCheck) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.checkNickname(requestCheck));
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<String> createMember(@Valid @RequestBody RequestMember requestMember, HttpSession session) {
        MemberDto memberDto = memberMapStruct.changeMemberDto(requestMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(memberDto, session));
    }

    // 전화번호, 주소 수정
    @PutMapping("/users")
    public ResponseEntity<ResponseMember> updateMember(@Valid @RequestBody RequestUpdate requestUpdate) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateMember(memberMapStruct.changeMemberDto(requestUpdate)));
    }

    // 비밀번호 수정
    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody RequestPassword requestPassword, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updatePassword(requestPassword, request));
    }

    // 회원 정보 조회
    @GetMapping("/users")
    public ResponseEntity<ResponseMember> getUser(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(memberMapStruct.changeResponse(memberService.getUser(request)));
    }

    // 사용자 쿠폰 조회
    @GetMapping("/myCoupon")
    public ResponseEntity<ResponseCoupon> getMyCoupon(HttpServletRequest request) {
        String uuid = request.getHeader("uuid");
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMyCoupon(uuid));
    }
}
