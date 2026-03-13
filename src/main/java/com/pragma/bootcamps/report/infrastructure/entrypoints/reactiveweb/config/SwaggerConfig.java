package com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Report Microservice",
                description = "Reports Management API",
                version = "1.0.0"
        )
)
public class SwaggerConfig {
}
