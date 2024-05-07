package com.example.paymentservice.payment.client;

import com.example.paymentservice.payment.vo.ResponseProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productService")
public interface ProductServiceClient {

    @GetMapping("/internal/{productUUID}")
    ResponseProduct existProduct(@PathVariable String productUUID);
}
