package com.hotelaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class HospedagemDTO {
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O tipo é obrigatório (HOTEL, MOTEL, POUSADA)")
    private String tipo;

    @NotBlank(message = "A cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório")
    private String estado;

    @NotNull(message = "A diária base é obrigatória")
    @Positive(message = "A diária base deve ser um valor positivo")
    private Double diariaBase;

    private Boolean disponivel;

    public HospedagemDTO() {}

    public HospedagemDTO(Long id, String nome, String tipo, String cidade, String estado, Double diariaBase, Boolean disponivel) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.cidade = cidade;
        this.estado = estado;
        this.diariaBase = diariaBase;
        this.disponivel = disponivel;
    }

    public static HospedagemDTOBuilder builder() {
        return new HospedagemDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Double getDiariaBase() { return diariaBase; }
    public void setDiariaBase(Double diariaBase) { this.diariaBase = diariaBase; }
    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }

    public static class HospedagemDTOBuilder {
        private Long id;
        private String nome;
        private String tipo;
        private String cidade;
        private String estado;
        private Double diariaBase;
        private Boolean disponivel;

        public HospedagemDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public HospedagemDTOBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public HospedagemDTOBuilder tipo(String tipo) {
            this.tipo = tipo;
            return this;
        }

        public HospedagemDTOBuilder cidade(String cidade) {
            this.cidade = cidade;
            return this;
        }

        public HospedagemDTOBuilder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public HospedagemDTOBuilder diariaBase(Double diariaBase) {
            this.diariaBase = diariaBase;
            return this;
        }

        public HospedagemDTOBuilder disponivel(Boolean disponivel) {
            this.disponivel = disponivel;
            return this;
        }

        public HospedagemDTO build() {
            return new HospedagemDTO(id, nome, tipo, cidade, estado, diariaBase, disponivel);
        }
    }
}
