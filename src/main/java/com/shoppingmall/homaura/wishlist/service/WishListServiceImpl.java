package com.shoppingmall.homaura.wishlist.service;

import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.member.repository.MemberRepository;
import com.shoppingmall.homaura.product.entity.Product;
import com.shoppingmall.homaura.product.repository.ProductRepository;
import com.shoppingmall.homaura.security.utils.SecurityUtil;
import com.shoppingmall.homaura.wishlist.dto.WishListDto;
import com.shoppingmall.homaura.wishlist.entity.WishList;
import com.shoppingmall.homaura.wishlist.mapstruct.WishListMapStruct;
import com.shoppingmall.homaura.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService{
    private final WishListRepository wishListRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final WishListMapStruct wishListMapStruct;

    @Override
    public int putList(WishListDto wishListDto) {
        String memberUUID = SecurityUtil.getCurrentMemberUUID();
        Member member = memberRepository.findByMemberUUID(memberUUID);
        Product product = productRepository.findByProductUUID(wishListDto.getProductUUID());

        WishList existingWishList = wishListRepository.findByProductAndMember(product, member);

        if (existingWishList != null) {
            // 이미 데이터가 있지만 오류를 내는것이 아닌 갯수를 추가하는 거라면 추가 로직이 필요하다
            throw new RuntimeException("이미 위시리스트에 담겨있는 상품입니다");
        }

        WishList wishList = WishList.builder()
                .member(member)
                .product(product)
                .unitCount(1)
                .build();

        wishListRepository.save(wishList);

        return 1;
    }

    @Override
    public int deleteList(Long wishListId) {
        Optional<WishList> wishList = wishListRepository.findById(wishListId);

        if (wishList.isEmpty()) {
            throw new RuntimeException("이미 삭제된 상품입니다");
        }

        wishListRepository.deleteById(wishListId);
        return 1;
    }

    @Transactional
    @Override
    public String updateProductUnitCount(String productUUID, int unitCount) {
        String memberUUID = SecurityUtil.getCurrentMemberUUID();
        Member member = memberRepository.findByMemberUUID(memberUUID);
        Product product = productRepository.findByProductUUID(productUUID);
        WishList aWishList = wishListRepository.findByProductAndMember(product, member);

        if (aWishList == null) {
            throw new RuntimeException("위시리스트에 담긴 상품이 아닙니다");
        }

        if (unitCount <= 0 || product.getStock() < unitCount) {
            throw new RuntimeException("상품의 주문량이 잘못되었습니다");
        }

        aWishList.updateUnitCount(unitCount);
        wishListRepository.save(aWishList);

        return "주문량이 수정되었습니다";
    }
}