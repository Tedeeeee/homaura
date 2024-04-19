package com.shoppingmall.homaura.product.repository;

import com.shoppingmall.homaura.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
