package com.example.productservice.product.controller;

import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.mapstruct.ProductMapStruct;
import com.example.productservice.product.repository.ProductRepository;
import com.example.productservice.product.service.ProductService;
import com.example.productservice.product.vo.RequestContent;
import com.example.productservice.product.vo.ResponseProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final ProductRepository productRepository;
    private final ProductMapStruct productMapStruct;
    private final ProductService productService;

    @GetMapping("/{productUUID}")
    public ResponseProduct existProduct(@PathVariable String productUUID) {
        Product byProductUUID = productRepository.findByProductUUID(productUUID);

        if (byProductUUID == null) {
            return null;
        }

        ProductDto productDto = productMapStruct.changeDto(byProductUUID);
        return productMapStruct.changeResponse(productDto);
    }

    @PutMapping("/increase")
    void increaseProductCount(@RequestBody RequestContent requestContent) {
        productService.increaseCount(requestContent);
    }
    @PutMapping("/decrease")
    void decreaseProductCount(@RequestBody RequestContent requestContent) {
        productService.decreaseCount(requestContent);
    }
}

