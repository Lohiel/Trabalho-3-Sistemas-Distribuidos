package com.hotelaria.model;

public class Hospedagem {
    private Long id;
    private String nome;
    private String tipo; 
    private String cidade;
    private String estado;
    private Double diariaBase;
    private Boolean disponivel;

    public Hospedagem() {}

    public Hospedagem(Long id, String nome, String tipo, String cidade, String estado, Double diariaBase, Boolean disponivel) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.cidade = cidade;
        this.estado = estado;
        this.diariaBase = diariaBase;
        this.disponivel = disponivel;
    }

    public static HospedagemBuilder builder() {
        return new HospedagemBuilder();
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

    public static class HospedagemBuilder {
        private Long id;
        private String nome;
        private String tipo;
        private String cidade;
        private String estado;
        private Double diariaBase;
        private Boolean disponivel;

        public HospedagemBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public HospedagemBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public HospedagemBuilder tipo(String tipo) {
            this.tipo = tipo;
            return this;
        }

        public HospedagemBuilder cidade(String cidade) {
            this.cidade = cidade;
            return this;
        }

        public HospedagemBuilder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public HospedagemBuilder diariaBase(Double diariaBase) {
            this.diariaBase = diariaBase;
            return this;
        }

        public HospedagemBuilder disponivel(Boolean disponivel) {
            this.disponivel = disponivel;
            return this;
        }

        public Hospedagem build() {
            return new Hospedagem(id, nome, tipo, cidade, estado, diariaBase, disponivel);
        }
    }
}
