package com.example.orderservice.wishList.controller;

import com.example.orderservice.wishList.service.WishListService;
import com.example.orderservice.wishList.vo.AddWishListForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishList")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    // 장바구니 확인
    @GetMapping("")
    public ResponseEntity<List<AddWishListForm>> getWishList(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.getWishList(request));
    }

    // 장바구니 추가
    @PostMapping("")
    public ResponseEntity<String> putWishList(@Valid @RequestBody AddWishListForm addWishListForm, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.putList(addWishListForm, request));
    }

    // 특정 상품 삭제
    @DeleteMapping("")
    public ResponseEntity<String> deleteWishProduct(@Valid @RequestBody AddWishListForm addWishListForm, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.deleteProduct(addWishListForm, request));
    }

    // 장바구니 삭제
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllWishList(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.deleteList(request));
    }

    // 특정 상품 갯수 추가
    @PutMapping("")
    public ResponseEntity<String> changeProductCount(@RequestBody AddWishListForm addWishListForm, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.changeUnitCount(addWishListForm, request));
    }
}
