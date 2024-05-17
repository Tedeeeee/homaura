package com.example.orderservice.order.client;

import com.example.orderservice.order.entity.Content;
import com.example.orderservice.order.vo.ResponseProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "productService")
public interface ProductServiceClient {

    @GetMapping("/internal/{productUUID}")
    ResponseProduct existProduct(@PathVariable String productUUID);

    @PutMapping("/internal/increase")
    void increaseCount(@RequestBody Content content);

    @PutMapping("/internal/decrease")
    void decreaseCount(@RequestBody List<Content> content);
}
