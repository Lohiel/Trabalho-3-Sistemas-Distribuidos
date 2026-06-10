package com.hotelaria.service;

import com.hotelaria.dto.*;
import com.hotelaria.model.*;
import com.hotelaria.repository.*;
import com.hotelaria.messaging.producer.ReservaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelariaService {

    private static final Logger log = LoggerFactory.getLogger(HotelariaService.class);

    private final ClienteRepository clienteRepository;
    private final HospedagemRepository hospedagemRepository;
    private final ReservaRepository reservaRepository;
    private final ReservaProducer reservaProducer;

    public HotelariaService(ClienteRepository clienteRepository,
                            HospedagemRepository hospedagemRepository,
                            ReservaRepository reservaRepository,
                            ReservaProducer reservaProducer) {
        this.clienteRepository = clienteRepository;
        this.hospedagemRepository = hospedagemRepository;
        this.reservaRepository = reservaRepository;
        this.reservaProducer = reservaProducer;
    }

    // --- CLIENTE METHODS ---

    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(this::toClienteDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO registrarCliente(ClienteDTO dto) {
        if (clienteRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new IllegalArgumentException("Cliente com este CPF já cadastrado.");
        }
        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .telefone(dto.getTelefone())
                .build();
        Cliente saved = clienteRepository.save(cliente);
        return toClienteDTO(saved);
    }

    // --- HOSPEDAGEM METHODS ---

    public List<HospedagemDTO> listarHospedagens() {
        return hospedagemRepository.findAll().stream()
                .map(this::toHospedagemDTO)
                .collect(Collectors.toList());
    }

    public HospedagemDTO registrarHospedagem(HospedagemDTO dto) {
        Hospedagem hospedagem = Hospedagem.builder()
                .nome(dto.getNome())
                .tipo(dto.getTipo())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .diariaBase(dto.getDiariaBase())
                .disponivel(dto.getDisponivel() != null ? dto.getDisponivel() : true)
                .build();
        Hospedagem saved = hospedagemRepository.save(hospedagem);
        return toHospedagemDTO(saved);
    }

    // --- RESERVA METHODS ---

    public List<ReservaDTO> listarReservas() {
        return reservaRepository.findAll().stream()
                .map(this::toReservaDTO)
                .collect(Collectors.toList());
    }

    public ReservaDTO buscarReservaPorId(Long id) {
        Reserva r = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com o ID: " + id));
        return toReservaDTO(r);
    }

    public ReservaDTO registrarReserva(ReservaRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        Hospedagem hospedagem = hospedagemRepository.findById(dto.getHospedagemId())
                .orElseThrow(() -> new IllegalArgumentException("Hospedagem não encontrada."));

        if (!hospedagem.getDisponivel()) {
            throw new IllegalStateException("Hospedagem selecionada não está disponível.");
        }

        long dias = ChronoUnit.DAYS.between(dto.getDataEntrada(), dto.getDataSaida());
        if (dias <= 0) {
            throw new IllegalArgumentException("A data de saída deve ser após a data de entrada.");
        }

        double valorTotal = dias * hospedagem.getDiariaBase();

        // 1. Save preliminary reservation with status CRIADA in the In-Memory Storage
        Reserva reserva = Reserva.builder()
                .cliente(cliente)
                .hospedagem(hospedagem)
                .dataEntrada(dto.getDataEntrada())
                .dataSaida(dto.getDataSaida())
                .valorTotal(valorTotal)
                .status(StatusReserva.CRIADA)
                .build();

        Reserva savedReserva = reservaRepository.save(reserva);

        // 2. Publish event to RabbitMQ for asynchronous processing
        reservaProducer.sendReservaEvent(savedReserva.getId(), "CRIADA");

        log.info("Reserva ID {} salva temporariamente como CRIADA. Evento publicado para RabbitMQ.", savedReserva.getId());

        return toReservaDTO(savedReserva);
    }

    public ReservaDTO cancelarReserva(Long id) {
        Reserva r = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada."));

        // Publish event to RabbitMQ for asynchronous cancellation processing
        reservaProducer.sendReservaEvent(r.getId(), "CANCELADA");
        log.info("Solicitação de cancelamento para Reserva ID {} enviada ao RabbitMQ.", r.getId());

        return toReservaDTO(r);
    }

    public ReservaDTO efetivarReserva(Long id) {
        Reserva r = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada."));

        if (r.getStatus() == StatusReserva.CANCELADA) {
            throw new IllegalStateException("Uma reserva cancelada não pode ser efetivada.");
        }

        // Publish event to RabbitMQ for asynchronous checkout processing
        reservaProducer.sendReservaEvent(r.getId(), "EFETIVADA");
        log.info("Solicitação de efetivação para Reserva ID {} enviada ao RabbitMQ.", r.getId());

        return toReservaDTO(r);
    }

    // --- DTO CONVERSIONS ---

    private ClienteDTO toClienteDTO(Cliente c) {
        if (c == null) return null;
        return ClienteDTO.builder()
                .id(c.getId())
                .nome(c.getNome())
                .cpf(c.getCpf())
                .telefone(c.getTelefone())
                .build();
    }

    private HospedagemDTO toHospedagemDTO(Hospedagem h) {
        if (h == null) return null;
        return HospedagemDTO.builder()
                .id(h.getId())
                .nome(h.getNome())
                .tipo(h.getTipo())
                .cidade(h.getCidade())
                .estado(h.getEstado())
                .diariaBase(h.getDiariaBase())
                .disponivel(h.getDisponivel())
                .build();
    }

    private ReservaDTO toReservaDTO(Reserva r) {
        if (r == null) return null;
        return ReservaDTO.builder()
                .id(r.getId())
                .cliente(toClienteDTO(r.getCliente()))
                .hospedagem(toHospedagemDTO(r.getHospedagem()))
                .dataEntrada(r.getDataEntrada())
                .dataSaida(r.getDataSaida())
                .valorTotal(r.getValorTotal())
                .status(r.getStatus().name())
                .build();
    }
}
