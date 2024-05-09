package com.example.productservice.product.controller;

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

    // 선착순 물건이라 재고를 미리 내림
    @PostMapping("/unique")
    public int decreaseItem(@RequestBody Content content) {
       return productService.decreaseCount(content);
    }

}
