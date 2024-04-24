package com.example.userservice.member.controller;

import com.example.userservice.member.dto.MemberDto;
import com.example.userservice.member.mapstruct.MemberMapStruct;
import com.example.userservice.member.service.MailService;
import com.example.userservice.member.service.MemberService;
import com.example.userservice.member.service.RedisService;
import com.example.userservice.member.vo.RequestMember;
import com.example.userservice.member.vo.RequestPassword;
import com.example.userservice.member.vo.RequestUpdate;
import com.example.userservice.member.vo.ResponseMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
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
    private final Environment env;
    private final RedisService redisService;

    // 이메일 인증 ( 중복된 이메일이 있을때 가입 불가능 )
    @GetMapping("/validationEmail")
    public ResponseEntity<String> sendEmail(@RequestParam String mail, HttpSession session) {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.sendEmail(mail, session));
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
    public ResponseEntity<String> updatePassword(@Valid @RequestBody RequestPassword requestPassword) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updatePassword(requestPassword));
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.logout(request));
    }

    // 회원 정보 조회
    @GetMapping("/users")
    public ResponseEntity<ResponseMember> getUser(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(memberMapStruct.changeResponse(memberService.getUser(request)));
    }

    @GetMapping("/getRedis")
    public String getValue(@RequestParam String key) {
        return redisService.getValue(key);
    }
}
