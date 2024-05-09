package com.example.productservice.product.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseProduct {
    private String productUUID;
    private String name;
    private int price;
    private int stock;
    private String producer;
    private boolean uniqueItem;
}
