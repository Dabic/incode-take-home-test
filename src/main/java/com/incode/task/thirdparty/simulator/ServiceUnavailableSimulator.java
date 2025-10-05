package com.incode.task.thirdparty.simulator;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

@Aspect
@Component
public class ServiceUnavailableSimulator {

    private static final String SERVICE_UNAVAILABLE_MESSAGE = "Simulated service unavailable";
    private static final int MAX_PROBABILITY = 100;
    /**
     * In production-grade setup, I wouldn't instantiate a class like this (using new) nor would I use {@link Random}.
     */
    private final Random RNG = new Random();

    public void maybeSimulateOutage(int outageProbability) {

        if (RNG.nextInt(MAX_PROBABILITY) < outageProbability) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, SERVICE_UNAVAILABLE_MESSAGE);
        }
    }
}
