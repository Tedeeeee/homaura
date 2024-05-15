package com.example.wishlistservice.wishlist.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ResponseWishList {
    private String productUUID;
    private String productName;
    private int unitCount;
}
