package com.example.productservice.product.service;

import com.example.productservice.product.entity.Content;
import com.example.productservice.product.entity.Product;
import com.example.productservice.product.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ProductRepository productRepository;

    @KafkaListener(topics = "product-topic")
    public void updateStock(String kafkaMessage) {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(map);
        Product product = productRepository.findByProductUUID((String) map.get("productUUID"));


        if (product != null) {
            product.decreaseStock((Integer) map.get("unitCount"));
            productRepository.save(product);
        }
    }
}
