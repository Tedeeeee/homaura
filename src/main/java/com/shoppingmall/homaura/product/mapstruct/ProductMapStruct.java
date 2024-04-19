package com.shoppingmall.homaura.product.mapstruct;

import com.shoppingmall.homaura.product.dto.ProductDto;
import com.shoppingmall.homaura.product.vo.ResponseProduct;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapStruct {

    ResponseProduct changeResponse(ProductDto productDto);

}
