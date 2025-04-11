package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BurntStolenIncidentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BurntStolenIncident getBurntStolenIncidentSample1() {
        return new BurntStolenIncident().id(1L).incidentId("incidentId1").type("type1").description("description1");
    }

    public static BurntStolenIncident getBurntStolenIncidentSample2() {
        return new BurntStolenIncident().id(2L).incidentId("incidentId2").type("type2").description("description2");
    }

    public static BurntStolenIncident getBurntStolenIncidentRandomSampleGenerator() {
        return new BurntStolenIncident()
            .id(longCount.incrementAndGet())
            .incidentId(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
