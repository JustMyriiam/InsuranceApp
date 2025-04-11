package com.insurance.app.domain;

import static com.insurance.app.domain.DriverTestSamples.*;
import static com.insurance.app.domain.TrafficViolationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrafficViolationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrafficViolation.class);
        TrafficViolation trafficViolation1 = getTrafficViolationSample1();
        TrafficViolation trafficViolation2 = new TrafficViolation();
        assertThat(trafficViolation1).isNotEqualTo(trafficViolation2);

        trafficViolation2.setId(trafficViolation1.getId());
        assertThat(trafficViolation1).isEqualTo(trafficViolation2);

        trafficViolation2 = getTrafficViolationSample2();
        assertThat(trafficViolation1).isNotEqualTo(trafficViolation2);
    }

    @Test
    void driverTest() {
        TrafficViolation trafficViolation = getTrafficViolationRandomSampleGenerator();
        Driver driverBack = getDriverRandomSampleGenerator();

        trafficViolation.setDriver(driverBack);
        assertThat(trafficViolation.getDriver()).isEqualTo(driverBack);

        trafficViolation.driver(null);
        assertThat(trafficViolation.getDriver()).isNull();
    }
}
