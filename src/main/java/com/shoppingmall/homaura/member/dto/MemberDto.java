package com.shoppingmall.homaura.member.dto;

import com.shoppingmall.homaura.order.vo.ResponseOrder;
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
    private List<ResponseOrder> orders;
}
