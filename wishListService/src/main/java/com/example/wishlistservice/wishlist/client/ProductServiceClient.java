package com.example.wishlistservice.wishlist.client;

import com.example.wishlistservice.wishlist.dto.WishListDto;
import com.example.wishlistservice.wishlist.vo.RequestWishList;
import com.example.wishlistservice.wishlist.vo.ResponseProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productService")
public interface ProductServiceClient {

    @GetMapping("/internal/{productUUID}")
    ResponseProduct existProduct(@PathVariable String productUUID);

    @GetMapping("/internal/checkCount")
    boolean checkStock(@RequestParam String productUUID,
                       @RequestParam int unitCount);
}
