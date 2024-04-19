package com.shoppingmall.homaura.product.service;

import com.shoppingmall.homaura.product.dto.ProductDto;

public interface ProductService {
    int createProduct(ProductDto productDto);
    ProductDto getProduct(String ProductUUID);
}
