package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ParkingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Parking getParkingSample1() {
        return new Parking().id(1L).parkingId("parkingId1").location("location1").capacity(1);
    }

    public static Parking getParkingSample2() {
        return new Parking().id(2L).parkingId("parkingId2").location("location2").capacity(2);
    }

    public static Parking getParkingRandomSampleGenerator() {
        return new Parking()
            .id(longCount.incrementAndGet())
            .parkingId(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .capacity(intCount.incrementAndGet());
    }
}
