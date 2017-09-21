package com.squeed.hoverfly;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.json.JSONException;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static io.specto.hoverfly.junit.core.SimulationSource.defaultPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimplestTest {

    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode();

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void time_frozen() throws JSONException {

        hoverflyRule.simulate(defaultPath("time.json"));

        ResponseEntity<String> response = testRestTemplate.getForEntity("/time", String.class);

        String expectedResponse =   "{\n" +
                "   \"time\": \"07:09:14 AM\",\n" +
                "   \"milliseconds_since_epoch\": 1505804954011,\n" +
                "   \"date\": \"09-19-2017\"\n" +
                "}\n";
        JSONAssert.assertEquals(expectedResponse, response.getBody(), false);
    }

}
