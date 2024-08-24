package org.layer;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(servers = {
        @Server(url = "https://api.layerapp.io", description = "운영서버"),
        @Server(url = "http://localhost:8080", description = "개발서버")})
@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableFeignClients
//@EnableFeignClients(basePackages = "org.layer.external.ai.infra")
public class LayerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LayerApplication.class, args);
    }
}