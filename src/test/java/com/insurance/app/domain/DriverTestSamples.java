package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DriverTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Driver getDriverSample1() {
        return new Driver()
            .id(1L)
            .fullName("fullName1")
            .dateOfBirth("dateOfBirth1")
            .licenseNumber("licenseNumber1")
            .licenseCategory("licenseCategory1")
            .address("address1")
            .phoneNumber("phoneNumber1")
            .yearsOfExperience(1)
            .accidentHistory("accidentHistory1");
    }

    public static Driver getDriverSample2() {
        return new Driver()
            .id(2L)
            .fullName("fullName2")
            .dateOfBirth("dateOfBirth2")
            .licenseNumber("licenseNumber2")
            .licenseCategory("licenseCategory2")
            .address("address2")
            .phoneNumber("phoneNumber2")
            .yearsOfExperience(2)
            .accidentHistory("accidentHistory2");
    }

    public static Driver getDriverRandomSampleGenerator() {
        return new Driver()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .dateOfBirth(UUID.randomUUID().toString())
            .licenseNumber(UUID.randomUUID().toString())
            .licenseCategory(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .yearsOfExperience(intCount.incrementAndGet())
            .accidentHistory(UUID.randomUUID().toString());
    }
}
