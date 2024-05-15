package com.example.wishlistservice.wishlist.service;


import com.example.wishlistservice.wishlist.dto.WishListDto;
import com.example.wishlistservice.wishlist.vo.ResponseWishList;

import java.util.List;

public interface WishListService {

    List<ResponseWishList> getMyList(String uuid);
    int putList(WishListDto wishListDto);

    int deleteWishList(String memberUUID);

    int deleteProduct(WishListDto wishListDto);

    int updateUnitCount(WishListDto wishListDto);
}
