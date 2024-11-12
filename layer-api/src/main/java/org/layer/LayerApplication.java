package org.layer;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@OpenAPIDefinition(servers = {
        @Server(url = "http://localhost:8080", description = "로컬서버"),
        @Server(url = "https://stgapi.layerapp.io", description = "개발서버"),
        @Server(url = "https://api.layerapp.io", description = "운영서버")
})
@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableFeignClients
@EnableAsync
public class LayerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LayerApplication.class, args);
    }

}