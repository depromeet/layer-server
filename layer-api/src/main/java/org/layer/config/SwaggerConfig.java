package org.layer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.layer.common.annotation.MemberId;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    SecurityScheme apiAuth = new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .in(SecurityScheme.In.HEADER)
            .name("authorization-token");

    SecurityRequirement addSecurityItem = new SecurityRequirement()
            .addList("authorization-token");

    @Bean
    public OpenAPI openAPI(){
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
                        .addSecuritySchemes("authorization-token", apiAuth)
                )
                .addSecurityItem(addSecurityItem)
                .info(info);
    }

    @Bean
    public OperationCustomizer customizeOperation() {
        return (operation, handlerMethod) -> {
            HandlerMethod method = (HandlerMethod) handlerMethod;
            method.getMethodParameters();
            method.getMethodParameters();
            if (Arrays.stream(method.getMethodParameters()).anyMatch(param -> param.hasParameterAnnotation(MemberId.class))) {
                operation.getParameters().removeIf(param -> "memberId".equals(param.getName()));
            }
            return operation;
        };
    }
}
