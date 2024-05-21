package com.example.wishlistservice.wishlist.controller;

import com.example.wishlistservice.wishlist.dto.WishListDto;
import com.example.wishlistservice.wishlist.service.WishListService;
import com.example.wishlistservice.wishlist.vo.RequestWishList;
import com.example.wishlistservice.wishlist.vo.ResponseWishList;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class WishListController {
    private final WishListService wishListService;
    private final Environment env;

    // 장바구니 확인
    @GetMapping("")
    public ResponseEntity<List<ResponseWishList>> getWishList(HttpServletRequest request) {
        String uuid = request.getHeader("uuid");
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.getMyList(uuid));
    }

    // 장바구니 추가
    @PostMapping("")
    public ResponseEntity<Integer> putWishList(@Valid @RequestBody RequestWishList requestWishList, HttpServletRequest request) {
        WishListDto wi = WishListDto.changeDto(requestWishList, request.getHeader("uuid"));
        return ResponseEntity.status(HttpStatus.CREATED).body(wishListService.putList(wi));
    }

    // 장바구니 전체 삭제
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Integer> deleteAllWishList(HttpServletRequest request) {
        String uuid = request.getHeader("uuid");
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.deleteWishList(uuid));
    }

    // 특정 상품 삭제
    @DeleteMapping("")
    public ResponseEntity<Integer> deleteWishProduct(@Valid @RequestBody RequestWishList requestWishList, HttpServletRequest request) {
        WishListDto wi = WishListDto.changeDto(requestWishList, request.getHeader("uuid"));
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.deleteProduct(wi));
    }

    // 특정 상품 수량 변경
    @PutMapping("")
    public ResponseEntity<Integer> updateUnitCount(@Valid @RequestBody RequestWishList requestWishList, HttpServletRequest request) {
        WishListDto wi = WishListDto.changeDto(requestWishList, request.getHeader("uuid"));
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.updateUnitCount(wi));
    }

    @GetMapping("/health_check")
    public String status() {
        return String.valueOf("It's Working in User Service"
                + ", port(local.server.port) = " + env.getProperty("local.server.port")
                + ", port(server.port) = " + env.getProperty("server.port")
                + ", token secret = " + env.getProperty("jwt.secret.key"));
    }
}
