package com.example.orderservice.wishList.service;

import com.example.orderservice.wishList.vo.AddWishListForm;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface WishListService {
    List<AddWishListForm> getWishList(HttpServletRequest request);
    String putList(AddWishListForm addWishListForm, HttpServletRequest request);
    String deleteProduct(AddWishListForm addWishListForm, HttpServletRequest request);
    String deleteList(HttpServletRequest request);
    String changeUnitCount(AddWishListForm addWishListForm, HttpServletRequest request);
}