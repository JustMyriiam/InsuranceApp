package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BlacklistedCarTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BlacklistedCar getBlacklistedCarSample1() {
        return new BlacklistedCar().id(1L).reason("reason1");
    }

    public static BlacklistedCar getBlacklistedCarSample2() {
        return new BlacklistedCar().id(2L).reason("reason2");
    }

    public static BlacklistedCar getBlacklistedCarRandomSampleGenerator() {
        return new BlacklistedCar().id(longCount.incrementAndGet()).reason(UUID.randomUUID().toString());
    }
}
