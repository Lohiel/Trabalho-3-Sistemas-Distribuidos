package com.hotelaria.dto;

import jakarta.validation.constraints.NotBlank;

public class ClienteDTO {
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    public ClienteDTO() {}

    public ClienteDTO(Long id, String nome, String cpf, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public static ClienteDTOBuilder builder() {
        return new ClienteDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public static class ClienteDTOBuilder {
        private Long id;
        private String nome;
        private String cpf;
        private String telefone;

        public ClienteDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ClienteDTOBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public ClienteDTOBuilder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        public ClienteDTOBuilder telefone(String telefone) {
            this.telefone = telefone;
            return this;
        }

        public ClienteDTO build() {
            return new ClienteDTO(id, nome, cpf, telefone);
        }
    }
}
