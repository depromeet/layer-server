package org.layer;

import org.junit.jupiter.api.Test;
import org.layer.domain.external.controller.ExternalApi;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {LayerApplication.class, ExternalApi.class})
@ActiveProfiles("test")
public class LayerApplicationTests {
    @Test
    void init() {

    }
}
