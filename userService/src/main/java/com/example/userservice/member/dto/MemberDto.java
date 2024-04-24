package com.example.userservice.member.dto;

import com.example.userservice.member.vo.ProductInfo;
import lombok.Data;

import java.util.List;

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
    private List<ProductInfo> wishLists;
}
