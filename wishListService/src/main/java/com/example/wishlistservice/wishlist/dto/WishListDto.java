package com.example.wishlistservice.wishlist.dto;

import com.example.wishlistservice.wishlist.vo.RequestWishList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishListDto {
    private String memberUUID;
    private String productUUID;
    private int unitCount;

    public static WishListDto changeDto(RequestWishList requestWishList, String uuid) {
        return new WishListDto(uuid, requestWishList.getProductUUID(), requestWishList.getUnitCount());
    }
}
