package com.example.paymentservice.payment.service;

import com.example.paymentservice.payment.vo.Content;
import com.example.paymentservice.payment.vo.RequestWishList;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface WishListService {

    List<RequestWishList> getMyList(HttpServletRequest request);
    String putList(RequestWishList requestWishList, HttpServletRequest request);
    String deleteProduct(RequestWishList requestWishList, HttpServletRequest request);
    String deleteWishList(HttpServletRequest request);
}
