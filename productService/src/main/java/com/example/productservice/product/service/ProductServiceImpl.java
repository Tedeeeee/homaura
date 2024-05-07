package com.example.productservice.product.service;

import com.example.productservice.product.dto.ProductDto;
import com.example.productservice.product.entity.Content;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.entity.Status;
import com.example.productservice.product.mapstruct.ProductMapStruct;
import com.example.productservice.product.repository.ProductRepository;
import com.example.productservice.product.vo.ResponseProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductMapStruct productMapStruct;
    private final ProductRepository productRepository;
    private final RedissonClient redissonClient;
    private final RedisService redisService;

    private int numbering;

    @Override
    public int createProduct(ProductDto productDto) {
        if (productDto.getReservationTime() != null) {
            productDto.setStatus(Status.CLOSE);
        }
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
    public ResponseProduct increaseCount(Content content) {
        RLock lock = redissonClient.getLock(content.getProductUUID());

        try{
            boolean available = lock.tryLock(5, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 획득 실패!");
            }
            Product product = productRepository.findByProductUUID(content.getProductUUID());

            if (product == null) {
                throw new IllegalArgumentException("제품을 찾을 수 없습니다");
            }

            if (product.getStock() < content.getUnitCount()) {
                throw new RuntimeException("재고가 부족하여 주문을 생성할 수 없습니다");
            }

            product.increaseStock(content.getUnitCount());

            productRepository.save(product);

            return productMapStruct.changeResponse(productMapStruct.changeDto(product));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public int decreaseCount(Content content) {
        Product product = productRepository.findByProductUUIDForUpdate(content.getProductUUID());

        if (product == null) {
            throw new RuntimeException("상품이 존재하지 않습니다");
        }

        if (product.getStock() < content.getUnitCount()) {
            throw new RuntimeException("상품의 재고가 남아있지 않습니다");
        }

        product.decreaseStock(content.getUnitCount());

        productRepository.save(product);

        return 1;
    }
}
