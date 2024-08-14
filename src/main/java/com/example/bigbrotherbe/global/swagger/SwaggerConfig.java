package com.example.bigbrotherbe.global.swagger;

import static com.example.bigbrotherbe.global.constant.Constant.Url.DOMAIN_URL;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
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
                        .description("My API Description"))
            .servers(List.of(
                new Server().url(DOMAIN_URL)  // HTTPS로 설정
                    .description("Production server")
            ));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("전체")
                .pathsToMatch("/api/v1/**")
                .build();
    }

}
