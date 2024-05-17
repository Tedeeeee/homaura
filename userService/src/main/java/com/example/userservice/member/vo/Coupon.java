package com.example.userservice.member.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Coupon {
    private String name;
    private int discount;
}
