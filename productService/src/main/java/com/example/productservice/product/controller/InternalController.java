package com.example.productservice.product.controller;

import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.entity.Content;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.mapstruct.ProductMapStruct;
import com.example.productservice.product.repository.ProductRepository;
import com.example.productservice.product.repository.RedisLockRepository;
import com.example.productservice.product.service.ProductService;
import com.example.productservice.product.vo.ResponseProduct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final ProductRepository productRepository;
    private final ProductMapStruct productMapStruct;
    private final ProductService productService;

    @Transactional
    @GetMapping("/{productUUID}")
    public ResponseProduct existProduct(@PathVariable String productUUID) {

        Product product = productRepository.findByProductUUID(productUUID);

        if (product == null) {
            throw new RuntimeException("존재하지 않는 상품입니다");
        }

        ProductDto productDto = productMapStruct.changeDto(product);
        return productMapStruct.changeResponse(productDto);
    }

    @PutMapping("/increase")
    public ResponseProduct increaseProductCount(@RequestBody Content content) {
        return productService.increaseCount(content);
    }

    // Radisson Lock
    @PutMapping("/decrease")
    public ResponseProduct decreaseProductCount(@RequestBody Content content) {
        return productService.decreaseCount(content);
    }

    // Pessimistic Lock
//    @Transactional
//    @PutMapping("/decrease")
//    public ResponseProduct decreaseProductCount(@RequestBody Content content) {
//        System.out.println("비관적 락");
//        Product product = productRepository.findByProductUUIDForUpdate(content.getProductUUID());
//
//        if (product == null) {
//            throw new IllegalArgumentException("제품을 찾을 수 없습니다");
//        }
//
//        if (product.getStock() < content.getUnitCount()) {
//            throw new RuntimeException("재고가 부족하여 주문을 생성할 수 없습니다");
//        }
//
//        product.decreaseStock(content.getUnitCount());
//
//        return productMapStruct.changeResponse(productMapStruct.changeDto(product));
//    }

    // Lettuce redis spin Lock
//    @PutMapping("/decrease")
//    public ResponseProduct decreaseProductCount(@RequestBody Content content) throws InterruptedException {
//        while (!redisLockRepository.lock(content.getProductUUID())) {
//            Thread.sleep(100);
//        }
//
//        try {
//            Product product = productRepository.findByProductUUID(content.getProductUUID());
//
//            if (product == null) {
//                throw new IllegalArgumentException("제품을 찾을 수 없습니다");
//            }
//
//            if (product.getStock() < content.getUnitCount()) {
//                throw new RuntimeException("재고가 부족하여 주문을 생성할 수 없습니다");
//            }
//
//            product.decreaseStock(content.getUnitCount());
//
//            productRepository.save(product);
//
//            return productMapStruct.changeResponse(productMapStruct.changeDto(product));
//
//        } finally {
//            redisLockRepository.unlock(content.getProductUUID());
//        }
//    }


}

