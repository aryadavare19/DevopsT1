package com.workerfinder.rest_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.workerfinder.restapi.RestApiApplication;

@SpringBootTest(classes = RestApiApplication.class)
class RestApiApplicationTests {

    @Test
    void contextLoads() {
        // Verifies Spring context starts without errors.
        // RMI is looked up lazily so no RMI server is needed here.
    }
}
