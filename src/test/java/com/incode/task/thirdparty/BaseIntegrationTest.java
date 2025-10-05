package com.incode.task.thirdparty;

import com.incode.task.thirdparty.simulator.ServiceUnavailableSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @MockitoBean
    protected ServiceUnavailableSimulator outageSimulator;
}
