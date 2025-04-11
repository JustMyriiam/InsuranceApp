package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TrafficViolationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TrafficViolation getTrafficViolationSample1() {
        return new TrafficViolation().id(1L).violationType("violationType1");
    }

    public static TrafficViolation getTrafficViolationSample2() {
        return new TrafficViolation().id(2L).violationType("violationType2");
    }

    public static TrafficViolation getTrafficViolationRandomSampleGenerator() {
        return new TrafficViolation().id(longCount.incrementAndGet()).violationType(UUID.randomUUID().toString());
    }
}
