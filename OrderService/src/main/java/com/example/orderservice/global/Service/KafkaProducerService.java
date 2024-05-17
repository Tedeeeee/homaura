package com.example.orderservice.global.Service;

import com.example.orderservice.order.dto.OrderDto;
import com.example.orderservice.order.entity.Content;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, List<Content> contents) {
        for (Content content : contents) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = "";

            try {
                jsonInString = mapper.writeValueAsString(content);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            kafkaTemplate.send(topic, jsonInString);
        }
    }
}
