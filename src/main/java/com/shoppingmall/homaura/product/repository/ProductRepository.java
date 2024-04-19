package com.shoppingmall.homaura.product.repository;

import com.shoppingmall.homaura.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductUUID(String productUUID);
    Slice<Product> findSliceBy(Pageable pageable);

    Page<Product> findPageByNameContaining(String productName, Pageable pageable);
}
