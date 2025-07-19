package org.layer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LayerBatchTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(LayerBatchTestApplication.class, args);
	}
}
