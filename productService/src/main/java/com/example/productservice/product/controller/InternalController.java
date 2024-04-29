package com.example.productservice.product.controller;

import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.entity.Content;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.mapstruct.ProductMapStruct;
import com.example.productservice.product.repository.ProductRepository;
import com.example.productservice.product.service.ProductService;
import com.example.productservice.product.vo.RequestContent;
import com.example.productservice.product.vo.ResponseProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
        Product byProductUUID = productRepository.findByProductUUIDForUpdate(productUUID);

        if (byProductUUID == null) {
            return null;
        }

        ProductDto productDto = productMapStruct.changeDto(byProductUUID);
        return productMapStruct.changeResponse(productDto);
    }

    @PutMapping("/increase")
    public void increaseProductCount(@RequestBody RequestContent requestContent) {
        productService.increaseCount(requestContent);
    }

    @Transactional
    @PutMapping("/decrease")
    public ResponseProduct decreaseProductCount(@RequestBody Content content) {
        Product product = productRepository.findByProductUUIDForUpdate(content.getProductUUID());

        if (product == null) {
            throw new IllegalArgumentException("제품을 찾을 수 없습니다");
        }

        if (product.getStock() < content.getUnitCount()) {
            throw new RuntimeException("재고가 부족하여 주문을 생성할 수 없습니다");
        }

        product.decreaseStock(content.getUnitCount());

        return productMapStruct.changeResponse(productMapStruct.changeDto(product));
    }
}

