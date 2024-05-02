package com.example.paymentservice.payment.client;

import com.example.paymentservice.payment.entity.Content;
import com.example.paymentservice.payment.vo.ResponseProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "productService")
public interface ProductServiceClient {

    @GetMapping("/internal/{productUUID}")
    ResponseProduct existProduct(@PathVariable String productUUID);

    @PutMapping("/internal/increase")
    void increaseCount(@RequestBody Content content);

    @PutMapping("/internal/decrease")
    ResponseProduct decreaseCount(@RequestBody Content content);

    @GetMapping("/internal/errorful/case1")
    ResponseEntity<String> case1();
}
