package org.layer.domain.etc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {
    @GetMapping("/greeting")
    public String greeting(@Value("${greeting.message}") String greeting) {
        return greeting;
    }
}
