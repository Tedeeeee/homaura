package com.example.productservice.product.service;

import com.example.productservice.product.entity.Content;
import com.example.productservice.product.entity.ProductStock;
import com.example.productservice.product.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductStockServiceImpl implements ProductStockService{

    private final ProductStockRepository productStockRepository;

    @Override
    @Transactional
    public void increaseCount(Content content) {
        ProductStock productStock = productStockRepository.findByProductUUIDForUpdate(content.getProductUUID());

        if (productStock == null) {
            throw new RuntimeException("상품이 존재하지 않습니다");
        }

        productStock.increaseStock(content.getUnitCount());
    }

    @Override
    @Transactional
    public void decreaseCount(Content content) {
        ProductStock productStock = productStockRepository.findByProductUUIDForUpdate(content.getProductUUID());

        if (productStock == null) {
            throw new RuntimeException("상품이 존재하지 않습니다");
        }

        if (productStock.getStock() < content.getUnitCount()) {
            throw new RuntimeException("상품의 재고가 남아있지 않습니다");
        }

        productStock.decreaseStock(content.getUnitCount());
    }

    @Override
    public boolean checkStock(String productUUID, int unitCount) {
        ProductStock productStock = productStockRepository.findByProductUUIDForUpdate(productUUID);

        int result = productStock.getStock() - unitCount;

        return result >= 0;
    }
}
