package com.squeed.hoverfly;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.badRequest;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.verification.HoverflyVerifications.times;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DslTest {

    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode();

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void time_dsl() {
        String expectedResponse =   "{\n" +
                "   \"time\": \"08:00:00 PM\",\n" +
                "   \"milliseconds_since_epoch\": 1505804954011,\n" +
                "   \"date\": \"01-01-2000\"\n" +
                "}\n";
        hoverflyRule.simulate(dsl(
                service("time.jsontest.com")
                        .get("/")
                        .willReturn(success(expectedResponse, "application/json"))));

        ResponseEntity<String> response = testRestTemplate.getForEntity("/time", String.class);

        assertEquals(expectedResponse, response.getBody());
        hoverflyRule.verify(
                service("time.jsontest.com")
                        .get("/"),
                times(1));
    }

    @Test
    public void time_delay() {
        String expectedResponse =   "{\n" +
                "   \"time\": \"08:00:00 PM\",\n" +
                "   \"milliseconds_since_epoch\": 1505804954011,\n" +
                "   \"date\": \"01-01-2000\"\n" +
                "}\n";
        hoverflyRule.simulate(dsl(
                service("time.jsontest.com")
                        .get("/")
                        .willReturn(success(expectedResponse, "application/json"))
                        .andDelay(1, TimeUnit.SECONDS)
                        .forAll()));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        testRestTemplate.getForEntity("/time", String.class);
        stopWatch.stop();

        assertTrue(stopWatch.getTotalTimeMillis() > 1000);
    }

    @Test
    public void time_error() {
        hoverflyRule.simulate(dsl(
                service("time.jsontest.com")
                        .get("/")
                        .willReturn(badRequest())));

        ResponseEntity<String> response = testRestTemplate.getForEntity("/time", String.class);
        assertTrue(response.getStatusCode().is5xxServerError());
    }

}
