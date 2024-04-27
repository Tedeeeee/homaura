package com.shoppingmall.homaura.product.repository;

import com.shoppingmall.homaura.product.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.productUUID=:productUUID")
    Product findByProductUUIDForUpdate(String productUUID);

    Product findByProductUUID(String productUUID);

    Slice<Product> findSliceBy(Pageable pageable);

    Page<Product> findPageByNameContaining(String productName, Pageable pageable);
}
