package com.example.orderservice.wishlist.service;

import com.example.orderservice.global.Service.RedisService;
import com.example.orderservice.order.client.ProductServiceClient;
import com.example.orderservice.order.vo.ResponseProduct;
import com.example.orderservice.wishlist.vo.AddWishListForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService{

    private final RedisService redisService;
    private final ProductServiceClient productServiceClient;

    @Override
    public String putList(AddWishListForm addWishListForm, HttpServletRequest request) {
        String uuid = request.getHeader("uuid");
        // 상품이 존재하는지 확인
        ResponseProduct responseProduct = productServiceClient.existProduct(addWishListForm.getProductUUID());

        if (responseProduct == null) {
            throw new RuntimeException("존재하지 않는 상품입니다");
        }


        // 현재 상품의 잔고보다 많이 주문하면 안된다
        if (responseProduct.getStock() < Integer.parseInt(addWishListForm.getUnitCount())) {
            throw new RuntimeException("물건이 부족합니다");
        }

        try {
            redisService.hsetValues(uuid, addWishListForm.getProductUUID(), addWishListForm.getUnitCount());
        } catch (Exception e) {
            e.printStackTrace();
            return "등록 실패";
        }

        return "등록 성공";
    }

    @Override
    public String changeUnitCount(AddWishListForm addWishListForm, HttpServletRequest request) {
        String uuid = request.getHeader("uuid");

        try {
            redisService.updateField(uuid, addWishListForm.getProductUUID(), addWishListForm.getUnitCount());
        } catch (Exception e) {
            e.printStackTrace();
            return "등록 실패";
        }

        return "수량 업데이트 성공";
    }

    @Override
    public String deleteProduct(AddWishListForm addWishListForm, HttpServletRequest request) {
        String uuid = request.getHeader("uuid");

        try {
            redisService.deleteField(uuid, addWishListForm.getProductUUID());
        } catch (Exception e) {
            e.printStackTrace();
            return "삭제 실패";
        }
        return "해당 상품이 제거되었습니다";
    }

    @Override
    public String deleteList(HttpServletRequest request) {
        String uuid = request.getHeader("uuid");

        try {
            redisService.deleteValue(uuid);
        } catch (Exception e) {
            e.printStackTrace();
            return "실패";
        }
        return "장바구니를 비웠습니다";
    }


}
