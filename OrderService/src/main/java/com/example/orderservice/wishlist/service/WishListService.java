package com.example.orderservice.wishlist.service;

import com.example.orderservice.wishlist.vo.AddWishListForm;
import jakarta.servlet.http.HttpServletRequest;

public interface WishListService {
    String putList(AddWishListForm addWishListForm, HttpServletRequest request);
    String deleteProduct(AddWishListForm addWishListForm, HttpServletRequest request);
    String deleteList(HttpServletRequest request);
    String changeUnitCount(AddWishListForm addWishListForm, HttpServletRequest request);
}
