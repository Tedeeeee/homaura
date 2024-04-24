package com.shoppingmall.homaura.wishlist.entity;

import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wishlist")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int unitCount;

    public void updateUnitCount(int unitCount) {
        if (unitCount <= 0) return;
        this.unitCount = unitCount;
    }
}
