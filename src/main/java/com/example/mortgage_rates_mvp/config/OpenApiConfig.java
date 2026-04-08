package com.example.mortgage_rates_mvp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mortgage Rates API")
                        .version("1.0")
                        .description("""
            This API allows users to retrieve mortgage interest rates and
            check if a mortgage is feasible based on income, home value, and loan parameters."""));
    }
}
