package com.example.bigbrotherbe.global.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("My API")
                        .version("1.0")
                        .description("My API Description"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("전체")
                .pathsToMatch("/api/big-brother/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memberControllerApi() {
        return GroupedOpenApi.builder()
                .group("Member Controller")
                .pathsToMatch("/api/big-brother/members/**")
                .build();
    }

    @Bean
    public GroupedOpenApi faqControllerApi() {
        return GroupedOpenApi.builder()
                .group("FAQ Controller")
                .pathsToMatch("/api/big-brother/faq/**")
                .build();
    }

}
