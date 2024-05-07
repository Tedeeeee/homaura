package com.example.paymentservice.payment.service;

import com.example.paymentservice.payment.client.ProductServiceClient;
import com.example.paymentservice.payment.vo.Content;
import com.example.paymentservice.payment.vo.RequestWishList;
import com.example.paymentservice.payment.vo.ResponseProduct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService{

    private final RedisService redisService;
    private final ProductServiceClient productServiceClient;

    @Override
    public List<RequestWishList> getMyList(HttpServletRequest request) {
        String uuid = request.getHeader("uuid");

        Map<String, String> allValue = redisService.getAllList(uuid);

        List<RequestWishList> list = new ArrayList<>();
        for (String s : allValue.keySet()) {
            int value = Integer.parseInt(allValue.get(s));
            if (value == 0) {
                redisService.deleteField(uuid, s);
                continue;
            }
            list.add(new RequestWishList(s, value));
        }
        return list;
    }

    @Override
    public String putList(RequestWishList requestWishList, HttpServletRequest request) {
        String uuid = request.getHeader("uuid");

        ResponseProduct product = productServiceClient.existProduct(requestWishList.getProductUUID());

        if (product == null) {
            throw new RuntimeException("존재하지 않는 상품입니다");
        }

        if (product.getStock() < requestWishList.getUnitCount()) {
            throw new RuntimeException("물건이 부족합니다");
        }

        try {
            redisService.hSetValues(uuid, requestWishList.getProductUUID(), String.valueOf(requestWishList.getUnitCount()));
        } catch (Exception e) {
            e.printStackTrace();
            return "등록 실패";
        }

        return "등록 성공";
    }

    @Override
    public String deleteProduct(RequestWishList requestWishList, HttpServletRequest request) {
        String uuid = request.getHeader("uuid");

        try {
            redisService.deleteField(uuid, requestWishList.getProductUUID());
        } catch (Exception e) {
            e.printStackTrace();
            return "삭제 실패";
        }
        return "해당 상품이 제거되었습니다";
    }

    @Override
    public String deleteWishList(HttpServletRequest request) {
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
