package com.shoppingmall.homaura.member.controller;

import com.shoppingmall.homaura.member.dto.MemberDto;
import com.shoppingmall.homaura.member.mapstruct.MemberMapStruct;
import com.shoppingmall.homaura.member.service.MailService;
import com.shoppingmall.homaura.member.service.MemberService;
import com.shoppingmall.homaura.member.vo.RequestMember;
import com.shoppingmall.homaura.member.vo.ResponseMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MemberController {

    private final MemberMapStruct memberMapStruct;
    private final MailService mailService;
    private final MemberService memberService;

    // 이메일 인증 ( 중복된 이메일이 있을때 가입 불가능 )
    @GetMapping("/validationEmail")
    public ResponseEntity<String> sendEmail(@RequestParam String mail) {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.sendEmail(mail));
    }

    // 인증 코드 확인
    @GetMapping("/checkCode")
    public ResponseEntity<String> checkCode(@RequestParam String code) {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.checkCode(code));
    }

    // 닉네임 중복 확인
    @GetMapping("/checkNickname")
    public ResponseEntity<String> checkNickname(@RequestParam String nickname) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.checkNickname(nickname));
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<String> createMember(@RequestBody RequestMember requestMemberDto) {
        MemberDto memberDto = memberMapStruct.changeMemberDto(requestMemberDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(memberDto));
    }
}