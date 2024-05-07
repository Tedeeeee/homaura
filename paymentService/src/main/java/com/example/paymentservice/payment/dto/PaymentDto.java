package com.example.paymentservice.payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDto {
    private String productUUID;
    private String memberUUID;
}
