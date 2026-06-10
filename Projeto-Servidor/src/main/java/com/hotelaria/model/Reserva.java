package com.hotelaria.model;

import java.time.LocalDate;

public class Reserva {
    private Long id;
    private Cliente cliente;
    private Hospedagem hospedagem;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private Double valorTotal;
    private StatusReserva status;

    public Reserva() {}

    public Reserva(Long id, Cliente cliente, Hospedagem hospedagem, LocalDate dataEntrada, LocalDate dataSaida, Double valorTotal, StatusReserva status) {
        this.id = id;
        this.cliente = cliente;
        this.hospedagem = hospedagem;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public static ReservaBuilder builder() {
        return new ReservaBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Hospedagem getHospedagem() { return hospedagem; }
    public void setHospedagem(Hospedagem hospedagem) { this.hospedagem = hospedagem; }
    public LocalDate getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }
    public LocalDate getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDate dataSaida) { this.dataSaida = dataSaida; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public StatusReserva getStatus() { return status; }
    public void setStatus(StatusReserva status) { this.status = status; }

    public static class ReservaBuilder {
        private Long id;
        private Cliente cliente;
        private Hospedagem hospedagem;
        private LocalDate dataEntrada;
        private LocalDate dataSaida;
        private Double valorTotal;
        private StatusReserva status;

        public ReservaBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ReservaBuilder cliente(Cliente cliente) {
            this.cliente = cliente;
            return this;
        }

        public ReservaBuilder hospedagem(Hospedagem hospedagem) {
            this.hospedagem = hospedagem;
            return this;
        }

        public ReservaBuilder dataEntrada(LocalDate dataEntrada) {
            this.dataEntrada = dataEntrada;
            return this;
        }

        public ReservaBuilder dataSaida(LocalDate dataSaida) {
            this.dataSaida = dataSaida;
            return this;
        }

        public ReservaBuilder valorTotal(Double valorTotal) {
            this.valorTotal = valorTotal;
            return this;
        }

        public ReservaBuilder status(StatusReserva status) {
            this.status = status;
            return this;
        }

        public Reserva build() {
            return new Reserva(id, cliente, hospedagem, dataEntrada, dataSaida, valorTotal, status);
        }
    }
}
