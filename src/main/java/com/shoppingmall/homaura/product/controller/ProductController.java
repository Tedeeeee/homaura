package com.shoppingmall.homaura.product.controller;

import com.shoppingmall.homaura.product.dto.ProductDto;
import com.shoppingmall.homaura.product.mapstruct.ProductMapStruct;
import com.shoppingmall.homaura.product.service.ProductService;
import com.shoppingmall.homaura.product.vo.RequestProduct;
import com.shoppingmall.homaura.product.vo.ResponseProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductMapStruct productMapStruct;
    private final ProductService productService;

    @PostMapping("/")
    public ResponseEntity<Integer> createProduct(@RequestBody RequestProduct requestProduct) {
        ProductDto productDto = productMapStruct.changeDto(requestProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDto));
    }

    @GetMapping("/{productUUID}")
    public ResponseEntity<ResponseProduct> getProduct(@PathVariable String productUUID) {
        ProductDto product = productService.getProduct(productUUID);
        return ResponseEntity.status(HttpStatus.OK).body(productMapStruct.changeResponse(product));
    }

    // 전체 상품 검색
    @GetMapping("/")
    public ResponseEntity<Slice<ResponseProduct>> getProductList(Pageable pageable) {
        Slice<ProductDto> products = productService.getProducts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(products.map(productMapStruct::changeResponse));
    }
}
