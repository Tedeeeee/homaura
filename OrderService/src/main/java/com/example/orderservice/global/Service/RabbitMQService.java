package com.example.orderservice.global.Service;

import com.example.orderservice.order.entity.Content;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RabbitMQService {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    private int numbering;

    public void sendStock(Content content) {
        log.info("{}번째 전송 stock sent: {}",++numbering, content.getUnitCount());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, content);
    }
}
