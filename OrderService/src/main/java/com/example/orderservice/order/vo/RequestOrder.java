package com.example.orderservice.order.vo;

import com.example.orderservice.order.entity.Content;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestOrder {
    private List<Content> products = new ArrayList<>();

    @NotNull(message = "정확한 주소를 작성해주세요")
    @Pattern(regexp = "^.{10,}$", message = "주소는 최소 10글자 이상이어야 합니다")
    private String deliveryAddress;

    @NotNull(message = "전화번호를 - 없이 작성해주세요")
    @Pattern(regexp = "^\\d{10,}$", message = "전화번호를 - 없이 10자리 이상의 숫자로 작성해주세요")
    private String deliveryPhone;
}
