package com.shoppingmall.homaura.product.service;

import com.shoppingmall.homaura.product.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductService {
    int createProduct(ProductDto productDto);
    ProductDto getProduct(String ProductUUID);
    Slice<ProductDto> getProducts(Pageable pageable);
    Page<ProductDto> getProductByName(String productName, int pageNum, int pageSize);
}
