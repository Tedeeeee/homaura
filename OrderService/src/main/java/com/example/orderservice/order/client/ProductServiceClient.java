package com.example.orderservice.order.client;

import com.example.orderservice.order.entity.Content;
import com.example.orderservice.order.vo.ResponseProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "productService")
public interface ProductServiceClient {

    @GetMapping("/internal/{productUUID}")
    ResponseProduct existProduct(@PathVariable String productUUID);

    @PutMapping("/internal/increase")
    void increaseCount(@RequestBody Content content);

    @PutMapping("/internal/decrease")
    ResponseProduct decreaseCount(@RequestBody Content content);
}
