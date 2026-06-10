package com.hotelaria.controller;

import com.hotelaria.dto.ReservaDTO;
import com.hotelaria.dto.ReservaRequestDTO;
import com.hotelaria.service.HotelariaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
@Tag(name = "Reservas", description = "Gerenciamento de reservas com comunicação assíncrona via RabbitMQ")
public class ReservaController {

    private final HotelariaService hotelariaService;

    public ReservaController(HotelariaService hotelariaService) {
        this.hotelariaService = hotelariaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as reservas")
    public ResponseEntity<List<ReservaDTO>> listar() {
        return ResponseEntity.ok(hotelariaService.listarReservas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de uma reserva pelo ID")
    public ResponseEntity<ReservaDTO> obterPorId(@PathVariable Long id) {
        return ResponseEntity.ok(hotelariaService.buscarReservaPorId(id));
    }

    @PostMapping
    @Operation(summary = "Solicitar nova reserva — salva como CRIADA e publica evento no RabbitMQ")
    public ResponseEntity<ReservaDTO> registrar(@Valid @RequestBody ReservaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelariaService.registrarReserva(dto));
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Solicitar cancelamento — publica evento de cancelamento no RabbitMQ")
    public ResponseEntity<ReservaDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(hotelariaService.cancelarReserva(id));
    }

    @PutMapping("/{id}/efetivar")
    @Operation(summary = "Solicitar efetivação (check-in) — publica evento de efetivação no RabbitMQ")
    public ResponseEntity<ReservaDTO> efetivar(@PathVariable Long id) {
        return ResponseEntity.ok(hotelariaService.efetivarReserva(id));
    }
}
