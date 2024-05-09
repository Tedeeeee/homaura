package com.example.orderservice.wishList.service;

import com.example.orderservice.wishList.vo.RequestWishList;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface WishListService {
    List<RequestWishList> getMyList(HttpServletRequest request);
    String putList(RequestWishList requestWishList, HttpServletRequest request);
    String deleteProduct(RequestWishList requestWishList, HttpServletRequest request);
    String deleteWishList(HttpServletRequest request);
}
