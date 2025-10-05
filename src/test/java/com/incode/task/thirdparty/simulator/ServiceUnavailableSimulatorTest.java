package com.incode.task.thirdparty.simulator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceUnavailableSimulatorTest {

    private static final int MAX_PROBABILITY = 100;

    @Mock
    private Random rng;

    @InjectMocks
    private ServiceUnavailableSimulator serviceUnavailableSimulator;

    @Test
    void shouldSimulateOutage() {

        ReflectionTestUtils.setField(serviceUnavailableSimulator, "RNG", rng);

        when(rng.nextInt(MAX_PROBABILITY))
                .thenReturn(50);

        assertThrows(ResponseStatusException.class, () -> serviceUnavailableSimulator.maybeSimulateOutage(51));
    }

    @Test
    void shouldNotSimulateOutage() {

        ReflectionTestUtils.setField(serviceUnavailableSimulator, "RNG", rng);

        when(rng.nextInt(MAX_PROBABILITY))
                .thenReturn(50);

        serviceUnavailableSimulator.maybeSimulateOutage(50);
    }
}
