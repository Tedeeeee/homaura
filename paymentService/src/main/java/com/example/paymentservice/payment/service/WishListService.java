package com.example.paymentservice.payment.service;

import com.example.paymentservice.payment.vo.AddWishListForm;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface WishListService {
    List<AddWishListForm> getWishList(HttpServletRequest request);
    String putList(AddWishListForm addWishListForm, HttpServletRequest request);
    String putUniqueItem(AddWishListForm addWishListForm, HttpServletRequest request);
    String deleteProduct(AddWishListForm addWishListForm, HttpServletRequest request);
    String deleteList(HttpServletRequest request);
    String changeUnitCount(AddWishListForm addWishListForm, HttpServletRequest request);
}
