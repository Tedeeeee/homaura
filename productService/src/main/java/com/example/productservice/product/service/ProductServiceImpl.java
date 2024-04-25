package com.example.productservice.product.service;

import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.mapstruct.ProductMapStruct;
import com.example.productservice.product.repository.ProductRepository;
import com.example.productservice.product.vo.RequestContent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductMapStruct productMapStruct;
    private final ProductRepository productRepository;

    @Override
    public int createProduct(ProductDto productDto) {
        Product product = productMapStruct.changeEntity(productDto);
        productRepository.save(product);
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
    public Page<ProductDto> getProductByName(String productName,int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Product> pageBy = productRepository.findPageByNameContaining(productName, pageable);
        return pageBy.map(productMapStruct::changeDto);
    }

    // internal 의 서비스
    @Override
    @Transactional
    public void increaseCount(RequestContent requestContent) {
        Product product = productRepository.findByProductUUID(requestContent.getProductUUID());

        product.increaseStock(requestContent.getUnitCount());
    }

    @Override
    @Transactional
    public void decreaseCount(RequestContent requestContent) {
        Product product = productRepository.findByProductUUID(requestContent.getProductUUID());

        product.decreaseStock(requestContent.getUnitCount());
    }

}
