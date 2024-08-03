package layer.domain;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"org.layer"})
@EnableJpaRepositories(basePackages = "org.layer.domain")
@EntityScan(basePackages = "org.layer.domain")
public class TestConfiguration {

}
