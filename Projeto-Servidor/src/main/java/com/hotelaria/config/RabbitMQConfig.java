package com.hotelaria.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE     = "hotel.reserva.direct";
    public static final String QUEUE_PROCESSAR = "queue.reserva.processar";
    public static final String QUEUE_NOTIFICAR = "queue.reserva.notificar";
    public static final String ROUTING_KEY  = "reserva.evento";

    @Bean
    public DirectExchange directExchange() {
        // durable=true: survives broker restarts; autoDelete=false: does not delete when no consumers
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue queueProcessar() {
        // durable=true: messages survive broker restarts (message persistence)
        return QueueBuilder.durable(QUEUE_PROCESSAR).build();
    }

    @Bean
    public Queue queueNotificar() {
        return QueueBuilder.durable(QUEUE_NOTIFICAR).build();
    }

    @Bean
    public Binding bindingProcessar(Queue queueProcessar, DirectExchange directExchange) {
        return BindingBuilder.bind(queueProcessar).to(directExchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding bindingNotificar(Queue queueNotificar, DirectExchange directExchange) {
        return BindingBuilder.bind(queueNotificar).to(directExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
