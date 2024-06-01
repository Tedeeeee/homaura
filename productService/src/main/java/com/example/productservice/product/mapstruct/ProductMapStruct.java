package com.example.productservice.product.mapstruct;

import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.vo.RequestProduct;
import com.example.productservice.product.vo.ResponseProduct;
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
