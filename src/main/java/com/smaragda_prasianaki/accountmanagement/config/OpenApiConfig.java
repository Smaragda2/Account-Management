package com.smaragda_prasianaki.accountmanagement.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI accountManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Account Management API")
                        .description("REST API for managing beneficiaries, accounts, and transactions.")
                        .version("1.0"))
                        .externalDocs(new ExternalDocumentation()
                            .description("GitHub Repository")
                            .url("https://github.com/Smaragda2/Account-Management"));
    }
}
