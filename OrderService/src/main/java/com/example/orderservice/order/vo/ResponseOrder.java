package com.example.orderservice.order.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.orderservice.order.entity.Content;
import com.example.orderservice.order.entity.Status;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrder {
    private String deliveryAddress;
    private String deliveryPhone;
    private String totalPrice;
    private Status status;
    private List<Content> productUUIDs;
}
