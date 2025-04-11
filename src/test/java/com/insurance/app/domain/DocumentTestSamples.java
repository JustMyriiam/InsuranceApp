package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Document getDocumentSample1() {
        return new Document().id(1L).documentName("documentName1").owner("owner1");
    }

    public static Document getDocumentSample2() {
        return new Document().id(2L).documentName("documentName2").owner("owner2");
    }

    public static Document getDocumentRandomSampleGenerator() {
        return new Document()
            .id(longCount.incrementAndGet())
            .documentName(UUID.randomUUID().toString())
            .owner(UUID.randomUUID().toString());
    }
}
