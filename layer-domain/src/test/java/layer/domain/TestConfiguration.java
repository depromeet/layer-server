package layer.domain;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.layer.domain.*.repository")
@EntityScan(basePackages = "org.layer.domain.*.entity")
public class TestConfiguration {

}
