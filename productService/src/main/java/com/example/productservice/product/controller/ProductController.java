package com.example.productservice.product.controller;

import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.mapstruct.ProductMapStruct;
import com.example.productservice.product.service.ProductService;
import com.example.productservice.product.vo.RequestProduct;
import com.example.productservice.product.vo.ResponseProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
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

    // 전체 상품 검색
    @GetMapping("")
    public ResponseEntity<Slice<ResponseProduct>> getProductList(Pageable pageable) {
        Slice<ProductDto> products = productService.getProducts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(products.map(productMapStruct::changeResponse));
    }

    // 검색한 상품 확인 시 페이징 처리
    @GetMapping("/{productName}/search")
    public ResponseEntity<Page<ResponseProduct>> getProductByName(@PathVariable String productName,
                                                                  @RequestParam(defaultValue = "0") int num,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Page<ProductDto> productByName = productService.getProductByName(productName, num, size);
        return ResponseEntity.status(HttpStatus.OK).body(productByName.map(productMapStruct::changeResponse));
    }

    @GetMapping("/health_check")
    public String status() {
        return String.valueOf("It's Working in User Service"
                + ", port(local.server.port) = " + env.getProperty("local.server.port")
                + ", port(server.port) = " + env.getProperty("server.port")
                + ", token secret = " + env.getProperty("token.secret")
                + ", token expiration time = " + env.getProperty("token.expiration_time"));
    }
}
