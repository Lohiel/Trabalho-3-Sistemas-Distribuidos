package com.hotelaria.model;

public class Cliente {
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;

    public Cliente() {}

    public Cliente(Long id, String nome, String cpf, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public static ClienteBuilder builder() {
        return new ClienteBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public static class ClienteBuilder {
        private Long id;
        private String nome;
        private String cpf;
        private String telefone;

        public ClienteBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ClienteBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public ClienteBuilder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        public ClienteBuilder telefone(String telefone) {
            this.telefone = telefone;
            return this;
        }

        public Cliente build() {
            return new Cliente(id, nome, cpf, telefone);
        }
    }
}
