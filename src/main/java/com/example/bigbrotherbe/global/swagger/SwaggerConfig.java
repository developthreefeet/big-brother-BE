package com.example.bigbrotherbe.global.swagger;

import static com.example.bigbrotherbe.global.constant.Constant.Url.DOMAIN_URL;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
                .info(apiInfo())
            .servers(httpsServer())
            .components(createComponents())
            .addSecurityItem(createSecurityRequirement());
    }

    private static SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement().addList("Authorization");
    }

    private static Components createComponents() {
        return new Components()
            .addSecuritySchemes("Authorization", new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
            );
    }

    private static List<Server> httpsServer() {
        return List.of(
            new Server().url(DOMAIN_URL)  // HTTPS로 설정
                .description("Production server")
        );
    }

    private static Info apiInfo() {
        return new Info().title("My API")
            .version("1.0")
            .description("My API Description");
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("전체")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
            .group("멤버 API")
            .pathsToMatch("/api/v1/members/**") // 멤버 관련 엔드포인트만 포함
            .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
            .group("어드민 API")
            .pathsToMatch("/api/v1/admin/**") // 관리자 관련 엔드포인트만 포함
            .build();
    }

}
