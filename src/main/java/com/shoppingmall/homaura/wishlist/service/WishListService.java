package com.shoppingmall.homaura.wishlist.service;

import com.shoppingmall.homaura.wishlist.dto.WishListDto;

public interface WishListService {
    int putList(WishListDto wishListDto);

    int deleteList(Long wishListId);

    String updateProductUnitCount(String productUUID, int unitCount);

}
