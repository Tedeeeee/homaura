package com.example.productservice.product.service;

import com.example.productservice.product.dto.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    int createProduct(ProductDto productDto);
    ProductDto getProduct(String ProductUUID);
    List<ProductDto> getProducts();
    Page<ProductDto> getProducts(int pageInt);
    Page<ProductDto> getProductByName(String productName, int pageNum);
}
