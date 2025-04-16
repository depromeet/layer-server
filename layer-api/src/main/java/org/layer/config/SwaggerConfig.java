package org.layer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.layer.common.annotation.DisableSwaggerSecurity;
import org.layer.common.annotation.MemberId;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SwaggerConfig {

    private static final String AUTH_TOKEN = "Authorization";

    SecurityScheme apiAuth = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .in(SecurityScheme.In.HEADER)
            .scheme("Bearer")
            .bearerFormat("JWT")
            .name(AUTH_TOKEN);

    SecurityRequirement addSecurityItem = new SecurityRequirement()
            .addList(AUTH_TOKEN);

    @Bean
    public OpenAPI openAPI() {
        var info = new Info();
        info.title("Layer API");
        info.description("Layer API 문서에요.");
        info.contact(
                new Contact()
                        .email("teamkb.dpm@gmail.com")
                        .name("떡잎마을방범대")
        );
        info.license(new License().name("MIT"));
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(AUTH_TOKEN, apiAuth)
                )
                .addSecurityItem(addSecurityItem)
                .info(info);
    }

    @Bean
    public OperationCustomizer customizeOperation() {
        return (operation, handlerMethod) -> {
            HandlerMethod method = handlerMethod;
            method.getMethodParameters();
            if (Arrays.stream(method.getMethodParameters()).anyMatch(param -> param.hasParameterAnnotation(MemberId.class))) {
                operation.getParameters().removeIf(param -> "memberId".equals(param.getName()));
            }
            return operation;
        };
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            DisableSwaggerSecurity methodAnnotation = handlerMethod.getMethodAnnotation(DisableSwaggerSecurity.class);
            if (methodAnnotation != null) {
                operation.setSecurity(Collections.emptyList());
            }
            return operation;
        };
    }
}