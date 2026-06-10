package com.hotelaria.seed;

import com.hotelaria.model.Cliente;
import com.hotelaria.model.Hospedagem;
import com.hotelaria.repository.ClienteRepository;
import com.hotelaria.repository.HospedagemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final HospedagemRepository hospedagemRepository;

    public DatabaseSeeder(ClienteRepository clienteRepository, HospedagemRepository hospedagemRepository) {
        this.clienteRepository = clienteRepository;
        this.hospedagemRepository = hospedagemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedHospedagens();
        seedClientes();
    }

    private void seedHospedagens() {
        if (hospedagemRepository.findAll().isEmpty()) {
            hospedagemRepository.save(Hospedagem.builder()
                    .nome("Hotel Central")
                    .tipo("HOTEL")
                    .cidade("Quixadá")
                    .estado("CE")
                    .diariaBase(220.0)
                    .disponivel(true)
                    .build());

            hospedagemRepository.save(Hospedagem.builder()
                    .nome("Pousada Serra")
                    .tipo("POUSADA")
                    .cidade("Quixadá")
                    .estado("CE")
                    .diariaBase(180.0)
                    .disponivel(true)
                    .build());

            hospedagemRepository.save(Hospedagem.builder()
                    .nome("Motel Premium")
                    .tipo("MOTEL")
                    .cidade("Quixadá")
                    .estado("CE")
                    .diariaBase(150.0)
                    .disponivel(true)
                    .build());

            System.out.println(">>> In-Memory Storage: Dados de Hospedagens semeados com sucesso! <<<");
        }
    }

    private void seedClientes() {
        if (clienteRepository.findAll().isEmpty()) {
            clienteRepository.save(Cliente.builder()
                    .nome("João Silva")
                    .cpf("123.456.789-00")
                    .telefone("(88) 99999-1111")
                    .build());

            clienteRepository.save(Cliente.builder()
                    .nome("Maria Souza")
                    .cpf("987.654.321-11")
                    .telefone("(88) 98888-2222")
                    .build());

            clienteRepository.save(Cliente.builder()
                    .nome("Carlos Lima")
                    .cpf("456.789.123-22")
                    .telefone("(88) 97777-3333")
                    .build());

            System.out.println(">>> In-Memory Storage: Dados de Clientes semeados com sucesso! <<<");
        }
    }
}
