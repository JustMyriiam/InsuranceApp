package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccidentHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccidentHistory getAccidentHistorySample1() {
        return new AccidentHistory().id(1L).accidentId("accidentId1").severity("severity1").description("description1");
    }

    public static AccidentHistory getAccidentHistorySample2() {
        return new AccidentHistory().id(2L).accidentId("accidentId2").severity("severity2").description("description2");
    }

    public static AccidentHistory getAccidentHistoryRandomSampleGenerator() {
        return new AccidentHistory()
            .id(longCount.incrementAndGet())
            .accidentId(UUID.randomUUID().toString())
            .severity(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
