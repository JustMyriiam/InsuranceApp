package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LocationRiskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LocationRisk getLocationRiskSample1() {
        return new LocationRisk().id(1L).region("region1");
    }

    public static LocationRisk getLocationRiskSample2() {
        return new LocationRisk().id(2L).region("region2");
    }

    public static LocationRisk getLocationRiskRandomSampleGenerator() {
        return new LocationRisk().id(longCount.incrementAndGet()).region(UUID.randomUUID().toString());
    }
}
