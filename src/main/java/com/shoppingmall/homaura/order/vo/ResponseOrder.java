package com.shoppingmall.homaura.order.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.homaura.order.entity.Content;
import com.shoppingmall.homaura.order.entity.Status;
import lombok.Data;

import java.util.ArrayList;
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
