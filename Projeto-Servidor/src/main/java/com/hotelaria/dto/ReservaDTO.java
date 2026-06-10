package com.hotelaria.dto;

import java.time.LocalDate;

public class ReservaDTO {
    private Long id;
    private ClienteDTO cliente;
    private HospedagemDTO hospedagem;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private Double valorTotal;
    private String status;

    public ReservaDTO() {}

    public ReservaDTO(Long id, ClienteDTO cliente, HospedagemDTO hospedagem, LocalDate dataEntrada, LocalDate dataSaida, Double valorTotal, String status) {
        this.id = id;
        this.cliente = cliente;
        this.hospedagem = hospedagem;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public static ReservaDTOBuilder builder() {
        return new ReservaDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ClienteDTO getCliente() { return cliente; }
    public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }
    public HospedagemDTO getHospedagem() { return hospedagem; }
    public void setHospedagem(HospedagemDTO hospedagem) { this.hospedagem = hospedagem; }
    public LocalDate getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }
    public LocalDate getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDate dataSaida) { this.dataSaida = dataSaida; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static class ReservaDTOBuilder {
        private Long id;
        private ClienteDTO cliente;
        private HospedagemDTO hospedagem;
        private LocalDate dataEntrada;
        private LocalDate dataSaida;
        private Double valorTotal;
        private String status;

        public ReservaDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ReservaDTOBuilder cliente(ClienteDTO cliente) {
            this.cliente = cliente;
            return this;
        }

        public ReservaDTOBuilder hospedagem(HospedagemDTO hospedagem) {
            this.hospedagem = hospedagem;
            return this;
        }

        public ReservaDTOBuilder dataEntrada(LocalDate dataEntrada) {
            this.dataEntrada = dataEntrada;
            return this;
        }

        public ReservaDTOBuilder dataSaida(LocalDate dataSaida) {
            this.dataSaida = dataSaida;
            return this;
        }

        public ReservaDTOBuilder valorTotal(Double valorTotal) {
            this.valorTotal = valorTotal;
            return this;
        }

        public ReservaDTOBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ReservaDTO build() {
            return new ReservaDTO(id, cliente, hospedagem, dataEntrada, dataSaida, valorTotal, status);
        }
    }
}
