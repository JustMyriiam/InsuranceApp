package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleAccessoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleAccessory getVehicleAccessorySample1() {
        return new VehicleAccessory().id(1L).accessoryId("accessoryId1").name("name1").type("type1");
    }

    public static VehicleAccessory getVehicleAccessorySample2() {
        return new VehicleAccessory().id(2L).accessoryId("accessoryId2").name("name2").type("type2");
    }

    public static VehicleAccessory getVehicleAccessoryRandomSampleGenerator() {
        return new VehicleAccessory()
            .id(longCount.incrementAndGet())
            .accessoryId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString());
    }
}
