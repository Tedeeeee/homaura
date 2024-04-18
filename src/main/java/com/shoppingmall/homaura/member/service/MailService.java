package com.shoppingmall.homaura.member.service;


import jakarta.servlet.http.HttpSession;

public interface MailService {
    String sendEmail(String email, HttpSession session);

    String checkCode(String checkCode);
}
