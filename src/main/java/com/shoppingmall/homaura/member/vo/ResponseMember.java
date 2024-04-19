package com.shoppingmall.homaura.member.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.homaura.wishlist.entity.WishList;
import lombok.*;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMember {
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String address;
    private List<WishList> wishLists;
}
