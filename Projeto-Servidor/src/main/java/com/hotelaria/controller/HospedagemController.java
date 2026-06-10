package com.hotelaria.controller;

import com.hotelaria.dto.HospedagemDTO;
import com.hotelaria.service.HotelariaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospedagens")
@CrossOrigin(origins = "*")
@Tag(name = "Hospedagens", description = "Gerenciamento de meios de hospedagem")
public class HospedagemController {

    private final HotelariaService hotelariaService;

    public HospedagemController(HotelariaService hotelariaService) {
        this.hotelariaService = hotelariaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as hospedagens disponíveis")
    public ResponseEntity<List<HospedagemDTO>> listar() {
        return ResponseEntity.ok(hotelariaService.listarHospedagens());
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma nova hospedagem")
    public ResponseEntity<HospedagemDTO> registrar(@Valid @RequestBody HospedagemDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelariaService.registrarHospedagem(dto));
    }
}
