package com.shoppingmall.homaura.member.dto;

import lombok.Data;

@Data
public class MemberDto {
    private String email;
    private String emailCode;
    private String password;
    private String name;
    private String nickname;
    private boolean nicknameVerified;
    private String phone;
    private String address;
}
