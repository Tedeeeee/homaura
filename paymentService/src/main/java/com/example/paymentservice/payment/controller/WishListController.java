package com.example.paymentservice.payment.controller;

import com.example.paymentservice.payment.service.WishListService;
import com.example.paymentservice.payment.vo.Content;
import com.example.paymentservice.payment.vo.RequestWishList;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishList")
public class WishListController {

    private final WishListService wishListService;

    // 장바구니 확인
    @GetMapping("")
    public ResponseEntity<List<RequestWishList>> getWishList(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.getMyList(request));
    }

    // 장바구니 추가
    @PostMapping("")
    public ResponseEntity<String> putWishList(@Valid @RequestBody RequestWishList requestWishList, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.putList(requestWishList, request));
    }

    // 장바구니 전체 삭제
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllWishList(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.deleteWishList(request));
    }

    // 특정 상품 삭제
    @DeleteMapping("")
    public ResponseEntity<String> deleteWishProduct(@Valid @RequestBody RequestWishList requestWishList, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.deleteProduct(requestWishList, request));
    }
}
