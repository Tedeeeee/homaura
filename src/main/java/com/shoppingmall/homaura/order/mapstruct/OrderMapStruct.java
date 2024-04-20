package com.shoppingmall.homaura.order.mapstruct;

import com.shoppingmall.homaura.order.dto.OrderDto;
import com.shoppingmall.homaura.order.vo.RequestOrder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapStruct {
    OrderDto changeDto(RequestOrder requestOrder);
}
