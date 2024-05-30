package com.example.productservice.product.service;

import com.example.productservice.product.dto.PageResponseDto;
import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.entity.ProductStock;
import com.example.productservice.product.mapstruct.ProductMapStruct;
import com.example.productservice.product.repository.ProductRepository;
import com.example.productservice.product.repository.ProductStockRepository;
import com.example.productservice.product.vo.ResponseProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductMapStruct productMapStruct;
    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;

    @Override
    public int createProduct(ProductDto productDto) {
        String productUUID = UUID.randomUUID().toString();
        Product product = Product.builder()
                .productUUID(productUUID)
                .name(productDto.getName())
                .createAt(LocalDateTime.now())
                .price(productDto.getPrice())
                .producer(productDto.getProducer())
                .build();

        ProductStock ps = ProductStock.builder()
                .productUUID(productUUID)
                .stock(productDto.getStock())
                .build();

        productRepository.save(product);
        productStockRepository.save(ps);

        return 1;
    }

    @Override
    public ProductDto getProduct(String productUUID) {
        Product product = productRepository.findByProductUUID(productUUID);

        if (product == null) {
            throw new RuntimeException("존재하지 않는 상품입니다");
        }

        return productMapStruct.changeDto(product);
    }

    @Override
    public Slice<ProductDto> getProducts(Pageable pageable) {
        Slice<Product> sliceBy = productRepository.findSliceBy(pageable);
        return sliceBy.map(productMapStruct::changeDto);
    }

    @Override
    public Page<ProductDto> getProductByName(String productName, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 20);
        Page<Product> pageBy = productRepository.findPageByNameContaining(productName, pageable);
        return pageBy.map(productMapStruct::changeDto);
    }
}
