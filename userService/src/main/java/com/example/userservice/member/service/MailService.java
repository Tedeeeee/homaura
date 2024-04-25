package com.example.userservice.member.service;


import com.example.userservice.member.vo.RequestCheck;

public interface MailService {
    String sendEmail(RequestCheck requestCheck);

    String checkCode(RequestCheck requestCheck);
}
