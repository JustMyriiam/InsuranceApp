package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentSinisterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentSinister getDocumentSinisterSample1() {
        return new DocumentSinister()
            .id(1L)
            .documentId("documentId1")
            .documentName("documentName1")
            .associatedSinister("associatedSinister1");
    }

    public static DocumentSinister getDocumentSinisterSample2() {
        return new DocumentSinister()
            .id(2L)
            .documentId("documentId2")
            .documentName("documentName2")
            .associatedSinister("associatedSinister2");
    }

    public static DocumentSinister getDocumentSinisterRandomSampleGenerator() {
        return new DocumentSinister()
            .id(longCount.incrementAndGet())
            .documentId(UUID.randomUUID().toString())
            .documentName(UUID.randomUUID().toString())
            .associatedSinister(UUID.randomUUID().toString());
    }
}
