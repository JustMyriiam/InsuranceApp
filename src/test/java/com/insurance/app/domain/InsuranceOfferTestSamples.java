package com.insurance.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InsuranceOfferTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InsuranceOffer getInsuranceOfferSample1() {
        return new InsuranceOffer()
            .id(1L)
            .offerId("offerId1")
            .offerName("offerName1")
            .coverageDetails("coverageDetails1")
            .termsAndConditions("termsAndConditions1");
    }

    public static InsuranceOffer getInsuranceOfferSample2() {
        return new InsuranceOffer()
            .id(2L)
            .offerId("offerId2")
            .offerName("offerName2")
            .coverageDetails("coverageDetails2")
            .termsAndConditions("termsAndConditions2");
    }

    public static InsuranceOffer getInsuranceOfferRandomSampleGenerator() {
        return new InsuranceOffer()
            .id(longCount.incrementAndGet())
            .offerId(UUID.randomUUID().toString())
            .offerName(UUID.randomUUID().toString())
            .coverageDetails(UUID.randomUUID().toString())
            .termsAndConditions(UUID.randomUUID().toString());
    }
}
