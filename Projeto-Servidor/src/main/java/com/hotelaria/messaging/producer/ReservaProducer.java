package com.hotelaria.messaging.producer;

import com.hotelaria.config.RabbitMQConfig;
import com.hotelaria.dto.ReservaEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReservaProducer {

    private static final Logger log = LoggerFactory.getLogger(ReservaProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public ReservaProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendReservaEvent(Long reservaId, String eventType) {
        ReservaEventMessage message = ReservaEventMessage.builder()
                .reservaId(reservaId)
                .eventType(eventType)
                .build();
        
        log.info("Publicando evento no RabbitMQ: {}", message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
    }
}
