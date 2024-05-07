package com.example.productservice.global.service;

import com.example.productservice.product.entity.Content;
import com.example.productservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RabbitMQService {

    private final ProductService productService;

    @RabbitListener(queues = "product.queue")
    public void receive(List<Content> contents) {
        for (Content content : contents) {
            productService.decreaseCount(content);
        }
    }
}
