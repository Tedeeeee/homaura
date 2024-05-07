package com.example.paymentservice.payment.controller;

import com.example.paymentservice.payment.vo.Content;
import com.example.paymentservice.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 선착순 단일 상품 구매
    @PostMapping("/direct")
    public void productPurchase(@RequestBody Content content, HttpServletRequest request) {
        paymentService.itemCount(content, request);
    }

    // 상품 재고 확인
    @GetMapping("")
    public ResponseEntity<Integer> getCount(@RequestParam String productUUID) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getStock(productUUID));
    }

    // 장바구니 상품 구매
    @PostMapping("/generate")
    public void purchase(@RequestBody List<Content> content, HttpServletRequest request) {
        paymentService.itemsCount(content, request);
    }

    // 주문서 발급


    // 예외 발생
    // 1. 상세 정보 입력 중 결제 취소 버튼 클릭

    // 2. 결제 실패
}
