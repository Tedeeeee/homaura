package com.shoppingmall.homaura.wishlist.mapstruct;

import com.shoppingmall.homaura.wishlist.dto.WishListDto;
import com.shoppingmall.homaura.wishlist.vo.RequestWishList;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WishListMapStruct {
    WishListDto changeDto(RequestWishList requestWishList);

}
