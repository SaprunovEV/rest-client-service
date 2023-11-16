package by.sapra.restclientservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@ContextConfiguration(classes = TestRestClientServiceApplication.class)
class RestClientServiceApplicationTests {

    @Autowired
    private PostgreSQLContainer<?> postgreSQLContainer;

    @BeforeEach
    void setUp() {
        postgreSQLContainer.start();
    }

    @AfterEach
    void tearDown() {
        postgreSQLContainer.stop();
    }

    @Test
    void contextLoads() {
    }

}
