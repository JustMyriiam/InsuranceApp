package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleUsageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static VehicleUsage getVehicleUsageSample1() {
        return new VehicleUsage().id(1L).usageType("usageType1").annualMileage(1);
    }

    public static VehicleUsage getVehicleUsageSample2() {
        return new VehicleUsage().id(2L).usageType("usageType2").annualMileage(2);
    }

    public static VehicleUsage getVehicleUsageRandomSampleGenerator() {
        return new VehicleUsage()
            .id(longCount.incrementAndGet())
            .usageType(UUID.randomUUID().toString())
            .annualMileage(intCount.incrementAndGet());
    }
}
