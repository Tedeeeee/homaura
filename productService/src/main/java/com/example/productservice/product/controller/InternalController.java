package com.example.productservice.product.controller;

import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.mapstruct.ProductMapStruct;
import com.example.productservice.product.repository.ProductRepository;
import com.example.productservice.product.vo.ResponseProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final ProductRepository productRepository;
    private final ProductMapStruct productMapStruct;

    @GetMapping("/{productUUID}")
    public ResponseProduct existProduct(@PathVariable String productUUID) {
        Product byProductUUID = productRepository.findByProductUUID(productUUID);

        if (byProductUUID == null) {
            return null;
        }

        ProductDto productDto = productMapStruct.changeDto(byProductUUID);
        return productMapStruct.changeResponse(productDto);
    }
}

