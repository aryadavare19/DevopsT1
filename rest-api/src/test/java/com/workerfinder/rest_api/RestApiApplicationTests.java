package com.workerfinder.rest_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Verifies the Spring application context loads without errors.
 * Previously skipped because WorkerController connected to RMI in its
 * constructor — that has been fixed; RMI is now looked up lazily per request.
 */
@SpringBootTest
class RestApiApplicationTests {

    @Test
    void contextLoads() {
        // If the context fails to start, this test will fail with a clear error.
    }
}
