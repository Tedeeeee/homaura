package com.example.productservice.product.service;

import com.example.productservice.product.entity.Content;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RabbitMQService {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @RabbitListener(queues = "product.queue")
    @Transactional
    public void receive(List<Content> contents) {
        for (Content content : contents) {
            Product product = productRepository.findByProductUUID(content.getProductUUID());

            if (product == null) {
                throw new RuntimeException("상품이 존재하지 않습니다");
            }

            if (product.getStock() < content.getUnitCount()) {
                throw new RuntimeException("상품의 재고가 남아있지 않습니다");
            }

            product.decreaseStock(content.getUnitCount());

            productRepository.save(product);
        }
    }
}
