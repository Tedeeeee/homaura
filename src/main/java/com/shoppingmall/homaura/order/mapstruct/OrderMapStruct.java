package com.shoppingmall.homaura.order.mapstruct;

import com.shoppingmall.homaura.order.dto.OrderDto;
import com.shoppingmall.homaura.order.entity.Order;
import com.shoppingmall.homaura.order.vo.RequestOrder;
import com.shoppingmall.homaura.order.vo.ResponseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapStruct {
    OrderDto changeDto(RequestOrder requestOrder);
    OrderDto changeDto(Order order);

    @Mapping(source = "productUUIDs", target = "products")
    List<ResponseOrder> changeResponseList(List<OrderDto> orderDto);
}
