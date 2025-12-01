package com.neo.tx.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI transactionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Transaction Validation API")
                        .version("v1")
                        .description("System for validating financial transactions")
                        .contact(new Contact()
                                .name("Kirill Tatarenko")
                                .email("one.overcoming@gmail.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://mit-license.org/")))
                .servers(List.of(new Server().url("/api/").description("Default server")));
    }
}
