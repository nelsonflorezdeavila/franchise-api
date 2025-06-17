package com.nequi.franchise.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(apiServers());
    }

    private Info apiInfo() {
        return new Info()
                .title("Franchise API")
                .description("API for managing franchises")
                .version("1.0.0")
                .contact(new Contact()
                        .name("API Support")
                        .email("support@nequi.com"));
    }

    private List<Server> apiServers() {
        return List.of(
                new Server()
                        .url("http://localhost:8080")
                        .description("Development server")
        );
    }
}
