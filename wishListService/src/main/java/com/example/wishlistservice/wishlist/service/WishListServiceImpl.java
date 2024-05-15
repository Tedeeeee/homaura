package com.example.wishlistservice.wishlist.service;

import com.example.wishlistservice.global.exception.BusinessExceptionHandler;
import com.example.wishlistservice.global.exception.ErrorCode;
import com.example.wishlistservice.wishlist.Entity.WishList;
import com.example.wishlistservice.wishlist.client.ProductServiceClient;
import com.example.wishlistservice.wishlist.dto.WishListDto;
import com.example.wishlistservice.wishlist.repository.WishListRepository;
import com.example.wishlistservice.wishlist.vo.ResponseProduct;
import com.example.wishlistservice.wishlist.vo.ResponseWishList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService{

    private final WishListRepository wishListRepository;
    private final ProductServiceClient productServiceClient;

    @Override
    @Transactional
    public List<ResponseWishList> getMyList(String uuid) {
        List<WishList> wishLists = wishListRepository.findByMemberUUID(uuid);

        List<ResponseWishList> responseWishLists = new ArrayList<>();
        // 각각의 상품의 이름을 가져오는 Feign 방식 사용
        for (WishList wishList : wishLists) {
            ResponseProduct responseProduct = productServiceClient.existProduct(wishList.getProductUUID());

            ResponseWishList response = ResponseWishList.builder()
                    .productUUID(responseProduct.getProductUUID())
                    .productName(responseProduct.getName())
                    .unitCount(wishList.getUnitCount())
                    .build();

            responseWishLists.add(response);
        }

        return responseWishLists;
    }

    @Override
    @Transactional
    public int putList(WishListDto wishListDto) {
        try {
            // 장바구니의 데이터에는 중복을 두지 않는다.
            if (wishListRepository.existsByMemberUUIDAndProductUUID(wishListDto.getMemberUUID(), wishListDto.getProductUUID())) {
                updateUnitCount(wishListDto);
                return 1;
            }

            // 주문 가능한지 확인 - Feign
            boolean available = productServiceClient.checkStock(wishListDto.getProductUUID(), wishListDto.getUnitCount());

            if (available) {
                WishList wishList = WishList.changeEntity(wishListDto);

                wishListRepository.save(wishList);
            } else {
                throw new BusinessExceptionHandler("재고가 부족합니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
            }
        } catch (Exception e) {
            throw new BusinessExceptionHandler(e.getMessage(), ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
        return 1;
    }

    @Override
    @Transactional
    public int deleteWishList(String memberUUID) {
        try {
            wishListRepository.deleteByMemberUUID(memberUUID);
        } catch (Exception e) {
            throw new BusinessExceptionHandler(e.getMessage(), ErrorCode.DELETE_ERROR);
        }
        return 1;
    }

    @Override
    @Transactional
    public int deleteProduct(WishListDto wishListDto) {
        try {
             wishListRepository.deleteByMemberUUIDAndProductUUID(wishListDto.getMemberUUID(), wishListDto.getProductUUID());
        } catch (Exception e) {
            throw new BusinessExceptionHandler(e.getMessage(), ErrorCode.DELETE_ERROR);
        }
        return 1;
    }

    @Override
    @Transactional
    public int updateUnitCount(WishListDto wishListDto) {
        try {
            // 주문 가능한지 확인 - Feign
            WishList wishList = wishListRepository.findByMemberUUIDAndProductUUID(wishListDto.getMemberUUID(), wishListDto.getProductUUID());

            int count = wishList.getUnitCount() + wishListDto.getUnitCount();
            boolean available = productServiceClient.checkStock(wishListDto.getProductUUID(), count);

            if (available) {
                wishList.changeUnitCount(count);
            } else {
                throw new BusinessExceptionHandler("재고가 부족합니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
            }
        } catch (Exception e) {
            throw new BusinessExceptionHandler(e.getMessage(), ErrorCode.UPDATE_ERROR);
        }

        return 1;
    }

}
