package com.shoppingmall.homaura.product.service;

import com.shoppingmall.homaura.product.dto.ProductDto;
import com.shoppingmall.homaura.product.entity.Product;
import com.shoppingmall.homaura.product.mapstruct.ProductMapStruct;
import com.shoppingmall.homaura.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

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
}
