package com.hotelaria.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Hotelaria Distribuída")
                        .version("1.0.0")
                        .description("""
                                API REST para gerenciamento de clientes, hospedagens e reservas.
                                
                                Utiliza armazenamento em memória (In-Memory Storage) e comunicação
                                assíncrona via RabbitMQ. Desenvolvido para os Trabalhos 3 e 4 da
                                disciplina de Sistemas Distribuídos.
                                """));
    }
}
