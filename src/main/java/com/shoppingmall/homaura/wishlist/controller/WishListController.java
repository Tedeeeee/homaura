package com.shoppingmall.homaura.wishlist.controller;

import com.shoppingmall.homaura.wishlist.dto.WishListDto;
import com.shoppingmall.homaura.wishlist.mapstruct.WishListMapStruct;
import com.shoppingmall.homaura.wishlist.service.WishListService;
import com.shoppingmall.homaura.wishlist.vo.RequestWishList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishList")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;
    private final WishListMapStruct wishListMapStruct;

    @PostMapping("")
    public ResponseEntity<Integer> putWishList(@RequestBody RequestWishList requestWishList) {
        WishListDto wishListDto = wishListMapStruct.changeDto(requestWishList);
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.putList(wishListDto));
    }

    @DeleteMapping("/{wishListId}")
    public ResponseEntity<Integer> deleteWishProduct(@PathVariable Long wishListId) {
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.deleteList(wishListId));
    }

    @PutMapping("/{productUUID}/{unitCount}")
    public ResponseEntity<String> updateProductUnitCount(@PathVariable int unitCount,
                                                         @PathVariable String productUUID) {
        return ResponseEntity.status(HttpStatus.OK).body(wishListService.updateProductUnitCount(productUUID, unitCount));
    }
}
