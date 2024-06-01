package com.example.productservice.product.repository;

import com.example.productservice.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByProductUUID(String productUUID);
    List<Product> findAll();
    Page<Product> findPageBy(Pageable pageable);

    Page<Product> findPageByNameContaining(String productName, Pageable pageable);
}
