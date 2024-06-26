package com.example.productservice.product.controller;

import com.example.productservice.product.dto.PageResponseDto;
import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.entity.Content;
import com.example.productservice.product.mapstruct.ProductMapStruct;
import com.example.productservice.product.service.ProductService;
import com.example.productservice.product.vo.RequestProduct;
import com.example.productservice.product.vo.ResponseProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ProductController {

    private final ProductMapStruct productMapStruct;
    private final ProductService productService;
    private final Environment env;

    @PostMapping("")
    public ResponseEntity<Integer> createProduct(@RequestBody RequestProduct requestProduct) {
        ProductDto productDto = productMapStruct.changeDto(requestProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDto));
    }

    @GetMapping("/{productUUID}")
    public ResponseEntity<ResponseProduct> getProduct(@PathVariable String productUUID) {
        ProductDto product = productService.getProduct(productUUID);
        return ResponseEntity.status(HttpStatus.OK).body(productMapStruct.changeResponse(product));
    }

    // 전체 상품 List 방식 검색
    @GetMapping("/list")
    public ResponseEntity<List<ResponseProduct>> getProductList() {
        List<ProductDto> products = productService.getProducts();
        List<ResponseProduct> list = products.stream()
                .map(productMapStruct::changeResponse)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    // 전체 상품 Page 방식 검색
    @GetMapping("/page")
    public ResponseEntity<Page<ResponseProduct>> getProductPage(@RequestParam(defaultValue = "0") int num) {
        Page<ProductDto> productByName = productService.getProducts(num);
        return ResponseEntity.status(HttpStatus.OK).body(productByName.map(productMapStruct::changeResponse));
    }

    // 전체 상품 캐싱
    @Cacheable(cacheNames = "products", key = "#num")
    @GetMapping("/redis")
    public PageResponseDto<ResponseProduct> getProductByName(@RequestParam(defaultValue = "0") int num) {
        Page<ProductDto> pageBy = productService.getProducts(num);
        List<ResponseProduct> responseProducts = pageBy.getContent().stream()
                .map(productMapStruct::changeResponse)
                .collect(Collectors.toList());

        return new PageResponseDto<>(
                responseProducts, pageBy.getNumber(), pageBy.getSize(), pageBy.getTotalElements()
        );
    }

    // 검색한 상품 확인 시 페이징 처리
    @GetMapping("/{productName}/search")
    public ResponseEntity<Page<ResponseProduct>> getProductByName(@PathVariable String productName,@RequestParam(defaultValue = "0") int num) {
        Page<ProductDto> pageBy = productService.getProductByName(productName, num);
        return ResponseEntity.status(HttpStatus.OK).body(pageBy.map(productMapStruct::changeResponse));
    }
}
