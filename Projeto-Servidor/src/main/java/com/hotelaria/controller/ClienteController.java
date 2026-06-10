package com.hotelaria.controller;

import com.hotelaria.dto.ClienteDTO;
import com.hotelaria.service.HotelariaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Gerenciamento de clientes hospedados")
public class ClienteController {

    private final HotelariaService hotelariaService;

    public ClienteController(HotelariaService hotelariaService) {
        this.hotelariaService = hotelariaService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os clientes cadastrados")
    public ResponseEntity<List<ClienteDTO>> listar() {
        return ResponseEntity.ok(hotelariaService.listarClientes());
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo cliente")
    public ResponseEntity<ClienteDTO> registrar(@Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelariaService.registrarCliente(dto));
    }
}
