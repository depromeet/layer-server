package org.layer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.layer.common", "org.layer.api", "org.layer.domain", "org.layer.external"})
@EnableJpaRepositories(basePackages = {"org.layer.domain"})
@EntityScan(basePackages = {"org.layer.domain"})
public class LayerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LayerApplication.class, args);
    }
}