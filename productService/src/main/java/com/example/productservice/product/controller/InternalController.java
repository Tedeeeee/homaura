package com.example.productservice.product.controller;

import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.entity.Content;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.mapstruct.ProductMapStruct;
import com.example.productservice.product.repository.ProductRepository;
import com.example.productservice.product.service.ProductService;
import com.example.productservice.product.vo.ResponseProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
        ResponseProduct responseProduct = productMapStruct.changeResponse(productDto);
        if (product.getReservationTime() != null) {
            responseProduct.setUniqueItem(true);
        }
        return responseProduct;
    }

    @PutMapping("/decrease")
    public void decreaseProductCount(@RequestBody List<Content> contents) {
        for (Content content : contents) productService.decreaseCount(content);
    }

    @PutMapping("/increase")
    public void increaseCount(@RequestBody Content content) {
        productService.increaseCount(content);
    }

    @GetMapping("/checkCount")
    boolean checkStock(@RequestParam String productUUID,
                       @RequestParam int unitCount) {
        return productService.checkStock(productUUID, unitCount);
    }
}

