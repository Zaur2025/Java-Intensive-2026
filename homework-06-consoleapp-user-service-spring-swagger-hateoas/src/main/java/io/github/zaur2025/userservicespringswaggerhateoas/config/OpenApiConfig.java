package io.github.zaur2025.userservicespringswaggerhateoas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .description("REST API для управления пользователями. Демонстрирует принципы HATEOAS.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Ваше имя")
                                .email("your.email@example.com")
                                .url("https://github.com/zaur2025"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}