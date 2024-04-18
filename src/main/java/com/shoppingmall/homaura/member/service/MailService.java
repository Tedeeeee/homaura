package com.shoppingmall.homaura.member.service;

import com.shoppingmall.homaura.member.dto.MailDto;

public interface MailService {
    String sendEmail(String email);
    String checkCode(String checkCode);
}
