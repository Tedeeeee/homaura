package com.shoppingmall.homaura.product.mapstruct;

import com.shoppingmall.homaura.product.dto.ProductDto;
import com.shoppingmall.homaura.product.entity.Product;
import com.shoppingmall.homaura.product.vo.RequestProduct;
import com.shoppingmall.homaura.product.vo.ResponseProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapStruct {

    ProductDto changeDto(RequestProduct requestProduct);

    @Mapping(target = "productUUID", expression = "java(java.util.UUID.randomUUID().toString())" )
    @Mapping(target = "createAt", expression = "java(java.time.LocalDateTime.now())")
    Product changeEntity(ProductDto productDto);
    ProductDto changeDto(Product product);


    ResponseProduct changeResponse(ProductDto productDto);

}
