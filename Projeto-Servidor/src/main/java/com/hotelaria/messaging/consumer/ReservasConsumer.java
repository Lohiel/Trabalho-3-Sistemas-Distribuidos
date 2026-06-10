package com.hotelaria.messaging.consumer;

import com.hotelaria.config.RabbitMQConfig;
import com.hotelaria.dto.ReservaEventMessage;
import com.hotelaria.model.Hospedagem;
import com.hotelaria.model.Reserva;
import com.hotelaria.model.StatusReserva;
import com.hotelaria.repository.HospedagemRepository;
import com.hotelaria.repository.ReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.consumers.enabled", havingValue = "true", matchIfMissing = true)
public class ReservasConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReservasConsumer.class);

    private final ReservaRepository reservaRepository;
    private final HospedagemRepository hospedagemRepository;

    public ReservasConsumer(ReservaRepository reservaRepository, HospedagemRepository hospedagemRepository) {
        this.reservaRepository = reservaRepository;
        this.hospedagemRepository = hospedagemRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROCESSAR)
    public synchronized void processarReserva(ReservaEventMessage message) {
        log.info("[Consumidor de Reservas] Mensagem recebida da fila queue.reserva.processar: {}", message);
        
        Reserva reserva = reservaRepository.findById(message.getReservaId()).orElse(null);
        if (reserva == null) {
            log.error("[Consumidor de Reservas] Reserva com ID {} não encontrada no In-Memory Storage.", message.getReservaId());
            return;
        }

        String eventType = message.getEventType();
        log.info("[Consumidor de Reservas] Processando evento '{}' para Reserva ID {}", eventType, reserva.getId());

        if ("CRIADA".equalsIgnoreCase(eventType)) {
            Hospedagem hospedagem = reserva.getHospedagem();
            if (hospedagem.getDisponivel()) {
                // Confirm booking
                hospedagem.setDisponivel(false);
                hospedagemRepository.save(hospedagem);
                
                reserva.setStatus(StatusReserva.PROCESSADA);
                reservaRepository.save(reserva);
                log.info("[Consumidor de Reservas] Reserva ID {} PROCESSADA e Hospedagem ID {} marcada como Indisponível.",
                        reserva.getId(), hospedagem.getId());
            } else {
                // If lodging became unavailable
                reserva.setStatus(StatusReserva.CANCELADA);
                reservaRepository.save(reserva);
                log.warn("[Consumidor de Reservas] Hospedagem ID {} já estava Indisponível. Reserva ID {} CANCELADA automaticamente.",
                        hospedagem.getId(), reserva.getId());
            }
        } else if ("CANCELADA".equalsIgnoreCase(eventType)) {
            Hospedagem hospedagem = reserva.getHospedagem();
            hospedagem.setDisponivel(true);
            hospedagemRepository.save(hospedagem);
            
            reserva.setStatus(StatusReserva.CANCELADA);
            reservaRepository.save(reserva);
            log.info("[Consumidor de Reservas] Reserva ID {} CANCELADA e Hospedagem ID {} liberada (Disponível).",
                    reserva.getId(), hospedagem.getId());
        } else if ("EFETIVADA".equalsIgnoreCase(eventType)) {
            reserva.setStatus(StatusReserva.EFETIVADA);
            reservaRepository.save(reserva);
            log.info("[Consumidor de Reservas] Reserva ID {} EFETIVADA com sucesso.", reserva.getId());
        }
    }
}
