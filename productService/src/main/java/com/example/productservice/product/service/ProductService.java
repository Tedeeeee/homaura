package com.example.productservice.product.service;

import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.vo.RequestContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductService {
    int createProduct(ProductDto productDto);
    ProductDto getProduct(String ProductUUID);
    Slice<ProductDto> getProducts(Pageable pageable);
    Page<ProductDto> getProductByName(String productName, int pageNum, int pageSize);

    // internal 의 서비스
    void increaseCount(RequestContent requestContent);
    void decreaseCount(RequestContent requestContent);
}
