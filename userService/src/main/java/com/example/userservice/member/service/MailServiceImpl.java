package com.example.userservice.member.service;

import com.example.userservice.member.dto.MailDto;
import com.example.userservice.member.repository.MemberRepository;
import com.example.userservice.member.utils.RandomNum;
import com.example.userservice.member.vo.RequestCheck;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;
    private final RedisService redisService;

    @Value("${spring.mail.username}")
    private String myEmail;

    public String sendEmail(RequestCheck requestCheck) {

        if (memberRepository.existsByEmail(requestCheck.getEmail())) {
            throw new RuntimeException("이미 가입된 회원입니다");
        }

        String code = RandomNum.validationCode();
        MailDto mailDto = new MailDto();
        mailDto.setUserMail(requestCheck.getEmail());
        mailDto.setTitle("homaura의 이메일 인증코드입니다");
        mailDto.setMessage("안녕하세요. homaura의 이메일 인증 코드입니다." + code + "를 입력해주세요");
        mailSend(mailDto);
        redisService.setValues(code, requestCheck.getEmail(), Duration.ofMinutes(10));
        return code;
    }

    public String checkCode(RequestCheck requestCheck) {
        String value = redisService.getValue(requestCheck.getEmailCode());
        if (!value.equals("")) return "인증이 완료되었습니다.";
        else return "인증에 실패하였습니다";
    }

    private void mailSend(MailDto mailDto) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false);

            messageHelper.setTo(mailDto.getUserMail());
            messageHelper.setSubject(mailDto.getTitle());
            messageHelper.setText(mailDto.getMessage());
            messageHelper.setFrom(myEmail);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
