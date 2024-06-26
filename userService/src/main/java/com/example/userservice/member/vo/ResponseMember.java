package com.example.userservice.member.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMember {
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String address;
}
