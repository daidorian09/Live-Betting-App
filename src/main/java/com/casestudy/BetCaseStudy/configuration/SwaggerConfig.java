package com.casestudy.BetCaseStudy.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Live Betting API")
                        .description("API documentation for the live betting case study")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement()
                        .addList("X-Customer-Id")
                        .addList("basicAuth"))
                .components(new Components()
                        .addSecuritySchemes("X-Customer-Id",
                                new SecurityScheme()
                                        .name("X-Customer-Id")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .description("Mock Customer ID header"))
                        .addSecuritySchemes("basicAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Server")
                ));
    }

    @Bean
    public GroupedOpenApi betSlipAndBulletinApi() {
        return GroupedOpenApi.builder()
                .group("controllers")
                .pathsToMatch("/api/betslips/**", "/api/bulletin/**")
                .build();
    }
}
