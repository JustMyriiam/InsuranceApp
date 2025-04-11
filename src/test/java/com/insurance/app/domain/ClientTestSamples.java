package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client()
            .id(1L)
            .clientId("clientId1")
            .fullName("fullName1")
            .dateOfBirth("dateOfBirth1")
            .address("address1")
            .phoneNumber("phoneNumber1")
            .email("email1")
            .clientType("clientType1");
    }

    public static Client getClientSample2() {
        return new Client()
            .id(2L)
            .clientId("clientId2")
            .fullName("fullName2")
            .dateOfBirth("dateOfBirth2")
            .address("address2")
            .phoneNumber("phoneNumber2")
            .email("email2")
            .clientType("clientType2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .clientId(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .dateOfBirth(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .clientType(UUID.randomUUID().toString());
    }
}
