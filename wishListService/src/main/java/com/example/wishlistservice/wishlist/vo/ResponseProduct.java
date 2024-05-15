package com.example.wishlistservice.wishlist.vo;


import lombok.Builder;
import lombok.Data;

@Data
public class ResponseProduct {
    private String productUUID;
    private String name;
    private int price;
    private int stock;
    private String producer;
    private boolean uniqueItem;
}
