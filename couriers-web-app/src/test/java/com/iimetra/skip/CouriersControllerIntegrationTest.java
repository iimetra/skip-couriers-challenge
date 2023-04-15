package com.iimetra.skip;

import static org.assertj.core.api.Assertions.assertThat;

import com.iimetra.skip.model.DeliveryTransactionsResponse;
import com.iimetra.skip.model.StatementResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = CouriersWebApplication.class)
@ExtendWith({SpringExtension.class})
@AutoConfigureTestDatabase(replace= Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = "classpath:db/delivery-records-test-data.sql")
public class CouriersControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/";
    }

    @Test
    void testGetCourierDeliveries() {
        assertThat(restTemplate.getForEntity(baseUrl + "/couriers/courier/deliveries?start=2023-04-15 20:00:00&end=2023-04-15 23:59:59", DeliveryTransactionsResponse.class))
            .hasFieldOrPropertyWithValue("status", 200)
            .hasFieldOrPropertyWithValue("body.courierId", "courier")
            .hasFieldOrPropertyWithValue("body.from", "2023-04-15 20:00:00")
            .hasFieldOrPropertyWithValue("body.to", "2023-04-15 23:59:59")
            .hasFieldOrPropertyWithValue("body.numberOfTransactions", 2);
    }

    @Test
    void testGetCourierStatement() {
        assertThat(restTemplate.getForEntity(baseUrl + "/couriers/courier/statements?start=2023-04-15 20:00:00&end=2023-04-15 23:59:59", StatementResponse.class))
            .hasFieldOrPropertyWithValue("status", 200)
            .hasFieldOrProperty("body.sum")
            .hasFieldOrProperty("body.deliveryIds")
            .hasFieldOrProperty("body.deliveryRecordIds");
    }
}
