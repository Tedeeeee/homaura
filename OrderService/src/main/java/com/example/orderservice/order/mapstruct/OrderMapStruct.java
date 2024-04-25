package com.example.orderservice.order.mapstruct;

import com.example.orderservice.order.dto.OrderDto;
import com.example.orderservice.order.entity.Order;
import com.example.orderservice.order.vo.RequestOrder;
import com.example.orderservice.order.vo.ResponseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapStruct {
    OrderDto changeDto(RequestOrder requestOrder);
    OrderDto changeDto(Order order);

    List<ResponseOrder> changeResponseList(List<OrderDto> orderDto);
}
