package com.shoppingmall.homaura.wishlist.repository;

import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.product.entity.Product;
import com.shoppingmall.homaura.wishlist.dto.WishListDto;
import com.shoppingmall.homaura.wishlist.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    WishList findByProductAndMember(Product product, Member member);
}
