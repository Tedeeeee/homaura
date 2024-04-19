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
    @JoinColumn(name = "memberUUID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "productUUID")
    private Product product;

    private Long unitCount;
}
