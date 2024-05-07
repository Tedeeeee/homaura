package com.example.paymentservice.payment.service;

import com.example.paymentservice.payment.vo.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQService {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    private final RabbitTemplate rabbitTemplate;

    public void changeStock(Content content) {
        rabbitTemplate.convertAndSend(exchangeName, "product.key", content);
    }

}
