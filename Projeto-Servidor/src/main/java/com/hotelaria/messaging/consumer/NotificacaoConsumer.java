package com.hotelaria.messaging.consumer;

import com.hotelaria.config.RabbitMQConfig;
import com.hotelaria.dto.ReservaEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.consumers.enabled", havingValue = "true", matchIfMissing = true)
public class NotificacaoConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICAR)
    public void enviarNotificacao(ReservaEventMessage message) {
        log.info("[Consumidor de Notificações] Mensagem recebida da fila queue.reserva.notificar: {}", message);
        
        Long reservaId = message.getReservaId();
        String eventType = message.getEventType();
        
        log.info("--------------------------------------------------------------------------------");
        log.info("[SIMULAÇÃO DE NOTIFICAÇÃO] Disparando Alerta para Cliente");
        log.info("Assunto: Atualização da sua Reserva (ID: {})", reservaId);
        log.info("Detalhe: O evento de status '{}' foi disparado com sucesso.", eventType);
        log.info("Meio: SMS / E-mail enviado!");
        log.info("--------------------------------------------------------------------------------");
    }
}
