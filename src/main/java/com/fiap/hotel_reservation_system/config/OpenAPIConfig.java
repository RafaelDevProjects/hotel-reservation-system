package com.fiap.hotel_reservation_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;


import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI hotelReservationOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Servidor de Desenvolvimento");

        Contact contact = new Contact();
        contact.setName("FIAP - 3ESPR 2025");
        contact.setEmail("damiana.costa@fiap.com.br");

        License license = new License();
        license.setName("Apache 2.0");
        license.setUrl("http://springdoc.org");

        Info info = new Info()
                .title("Hotel Reservation System API")
                .version("1.0.0")
                .contact(contact)
                .license(license)
                .description("""
                    Sistema completo de reservas de hotel com gerenciamento de quartos, 
                    reservas, check-in, check-out e validações de negócio.
                    
                    ## Funcionalidades:
                    - ✅ Gerenciamento de quartos
                    - ✅ Reservas com validação de datas
                    - ✅ Check-in e check-out
                    - ✅ Cálculo automático de valores
                    - ✅ Validação de disponibilidade
                    """);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
