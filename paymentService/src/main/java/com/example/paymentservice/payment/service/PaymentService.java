package com.example.paymentservice.payment.service;

import com.example.paymentservice.payment.vo.Content;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PaymentService {
    void itemCount(Content content, HttpServletRequest request);
    void itemsCount(List<Content> content, HttpServletRequest request);
    int getStock(String productUUID);
}
