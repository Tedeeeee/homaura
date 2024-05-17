package com.example.wishlistservice.wishlist.repository;

import com.example.wishlistservice.wishlist.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    void deleteByMemberUUID(String memberUUID);
    void deleteByMemberUUIDAndProductUUID(String memberUUID, String productUUID);
    WishList findByMemberUUIDAndProductUUID(String memberUUID, String productUUID);
    boolean existsByMemberUUIDAndProductUUID(String memberUUID, String productUUID);
    List<WishList> findByMemberUUID(String memberUUID);
}
