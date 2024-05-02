package com.example.paymentservice.payment.service;

import com.example.paymentservice.global.Service.RedisService;
import com.example.paymentservice.payment.client.ProductServiceClient;
import com.example.paymentservice.payment.vo.ResponseProduct;
import com.example.paymentservice.payment.vo.AddWishListForm;
import jakarta.servlet.http.HttpServletRequest;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService{

    private final RedisService redisService;
    private final ProductServiceClient productServiceClient;
    private final RedissonClient redissonClient;

    @Override
    public List<AddWishListForm> getWishList(HttpServletRequest request) {
        String uuid = request.getHeader("uuid");

        Map<String, String> allValues = redisService.getAllValues(uuid);

        List<AddWishListForm> list = new ArrayList<>();
        for (String s : allValues.keySet()) {
            if (allValues.get(s).equals("0")) {
                redisService.deleteField(uuid, s);
                continue;
            }
            list.add(new AddWishListForm(s, allValues.get(s)));
        }

        return list;
    }

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
            redisService.hSetValues(uuid, addWishListForm.getProductUUID(), addWishListForm.getUnitCount());
        } catch (Exception e) {
            e.printStackTrace();
            return "등록 실패";
        }

        return "등록 성공";
    }

    // 예약 상품 판매를 위한 장바구니
    @Override
    public String putUniqueItem(AddWishListForm addWishListForm, HttpServletRequest request) {
        RLock lock = redissonClient.getLock(addWishListForm.getProductUUID());

        //String uuid = request.getHeader("uuid");
        // 상품이 존재하는지 확인
        int value = Integer.parseInt(redisService.getValue(addWishListForm.getProductUUID()));
        if (value <= 0) {
            throw new RuntimeException("물건의 재고가 부족합니다");
        }

        try {
            boolean available = lock.tryLock(5, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 획득 실패!");
            }
            //redisService.hsetValues(uuid, addWishListForm.getProductUUID(), addWishListForm.getUnitCount());

            // 테스트 용
            String name = Thread.currentThread().getName();
            System.out.println("name = " + name);
            redisService.hSetUniqueValues(name, addWishListForm.getProductUUID(), addWishListForm.getUnitCount());
            redisService.decreaseValue(addWishListForm.getProductUUID());
        } catch (Exception e) {
            e.printStackTrace();
            return "등록 실패";
        } finally {
            lock.unlock();
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
