package com.shoppingmall.homaura.member.controller;

import com.shoppingmall.homaura.member.service.MailService;
import com.shoppingmall.homaura.member.vo.RequestMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MemberController {

    private final MailService mailService;

    @GetMapping("/validationEmail/{mail}")
    public ResponseEntity<String> sendEmail(@PathVariable String mail) {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.sendEmail(mail));
    }

    @GetMapping("/checkCode/{code}")
    public ResponseEntity<String> checkCode(@PathVariable String code) {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.checkCode(code));
    }

    @PostMapping("/signup")
    public void signup(@RequestBody RequestMember requestMemberDto) {
        // 컨트롤러에서 Request -> Dto, Dto -> Response 로 변환되어야 한다.
        // 서비스에서는 Dto -> Entity, Entity -> Dto
    }
}
