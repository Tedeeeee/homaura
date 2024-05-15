package com.example.wishlistservice.wishlist.Entity;

import com.example.wishlistservice.wishlist.dto.WishListDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "wishlist")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberUUID;
    private String productUUID;
    private int unitCount;

    public void changeUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public static WishList changeEntity(WishListDto wishListDto) {
        return WishList.builder()
                .memberUUID(wishListDto.getMemberUUID())
                .productUUID(wishListDto.getProductUUID())
                .unitCount(wishListDto.getUnitCount())
                .build();
    }
}

