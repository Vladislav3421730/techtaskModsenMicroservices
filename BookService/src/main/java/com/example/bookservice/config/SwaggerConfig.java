package com.example.bookservice.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Book Service",
                description = "service for working with books, including CRUD operations and searching by id and isbn",
                version = "1.0.0",
                contact = @Contact(
                        name = "Panasik Uladzislau",
                        email = "panasikvladislav1@gmail.com",
                        url = "https://github.com/Vladislav3421730"
                )
        )
)
public class SwaggerConfig {
}
