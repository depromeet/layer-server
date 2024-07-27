package org.layer;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(servers = {
        @Server(url = "https://api.layerapp.io", description = "운영서버"),
        @Server(url = "http://localhost:8080", description = "개발서버")})
@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {"org.layer.domain.common"})
public class LayerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LayerApplication.class, args);
    }
}