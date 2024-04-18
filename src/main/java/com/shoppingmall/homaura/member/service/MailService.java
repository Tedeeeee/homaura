package com.shoppingmall.homaura.member.service;


public interface MailService {
    String sendEmail(String email);
    String checkCode(String checkCode);
}
