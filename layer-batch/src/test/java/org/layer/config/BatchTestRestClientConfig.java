package org.layer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BatchTestRestClientConfig {
	@Bean
	public RestClient restClient() {
		return RestClient.create();
	}
}
