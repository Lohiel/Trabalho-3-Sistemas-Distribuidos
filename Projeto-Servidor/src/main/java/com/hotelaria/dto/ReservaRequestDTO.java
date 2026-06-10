package com.hotelaria.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReservaRequestDTO {

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O ID da hospedagem é obrigatório")
    private Long hospedagemId;

    @NotNull(message = "A data de entrada é obrigatória")
    private LocalDate dataEntrada;

    @NotNull(message = "A data de saída é obrigatória")
    private LocalDate dataSaida;

    public ReservaRequestDTO() {}

    public ReservaRequestDTO(Long clienteId, Long hospedagemId, LocalDate dataEntrada, LocalDate dataSaida) {
        this.clienteId = clienteId;
        this.hospedagemId = hospedagemId;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }

    public static ReservaRequestDTOBuilder builder() {
        return new ReservaRequestDTOBuilder();
    }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getHospedagemId() { return hospedagemId; }
    public void setHospedagemId(Long hospedagemId) { this.hospedagemId = hospedagemId; }
    public LocalDate getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }
    public LocalDate getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDate dataSaida) { this.dataSaida = dataSaida; }

    public static class ReservaRequestDTOBuilder {
        private Long clienteId;
        private Long hospedagemId;
        private LocalDate dataEntrada;
        private LocalDate dataSaida;

        public ReservaRequestDTOBuilder clienteId(Long clienteId) {
            this.clienteId = clienteId;
            return this;
        }

        public ReservaRequestDTOBuilder hospedagemId(Long hospedagemId) {
            this.hospedagemId = hospedagemId;
            return this;
        }

        public ReservaRequestDTOBuilder dataEntrada(LocalDate dataEntrada) {
            this.dataEntrada = dataEntrada;
            return this;
        }

        public ReservaRequestDTOBuilder dataSaida(LocalDate dataSaida) {
            this.dataSaida = dataSaida;
            return this;
        }

        public ReservaRequestDTO build() {
            return new ReservaRequestDTO(clienteId, hospedagemId, dataEntrada, dataSaida);
        }
    }
}
