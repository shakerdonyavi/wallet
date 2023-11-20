package com.bh.account.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion,
                                 @Value("${springdoc.server.url}") String serverUrl) {
        Server server = new Server()
                .url(serverUrl)
                .description("Server URL in Development environment");
        Info info = new Info()
                .title("Account Service API")
                .version(appVersion)
                .description("This API exposes endpoints to create account for customers and make transactions");
        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
