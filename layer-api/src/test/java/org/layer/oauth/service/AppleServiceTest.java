package org.layer.oauth.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppleServiceTest {
    @Autowired
    AppleService appleService;
    @Test
    void appleServiceTest() {
        appleService.generateClientSecret();
    }

}