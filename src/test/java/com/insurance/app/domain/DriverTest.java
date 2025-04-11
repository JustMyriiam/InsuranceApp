package com.insurance.app.domain;

import static com.insurance.app.domain.ContractTestSamples.*;
import static com.insurance.app.domain.DriverTestSamples.*;
import static com.insurance.app.domain.TrafficViolationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DriverTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Driver.class);
        Driver driver1 = getDriverSample1();
        Driver driver2 = new Driver();
        assertThat(driver1).isNotEqualTo(driver2);

        driver2.setId(driver1.getId());
        assertThat(driver1).isEqualTo(driver2);

        driver2 = getDriverSample2();
        assertThat(driver1).isNotEqualTo(driver2);
    }

    @Test
    void trafficViolationTest() {
        Driver driver = getDriverRandomSampleGenerator();
        TrafficViolation trafficViolationBack = getTrafficViolationRandomSampleGenerator();

        driver.addTrafficViolation(trafficViolationBack);
        assertThat(driver.getTrafficViolations()).containsOnly(trafficViolationBack);
        assertThat(trafficViolationBack.getDriver()).isEqualTo(driver);

        driver.removeTrafficViolation(trafficViolationBack);
        assertThat(driver.getTrafficViolations()).doesNotContain(trafficViolationBack);
        assertThat(trafficViolationBack.getDriver()).isNull();

        driver.trafficViolations(new HashSet<>(Set.of(trafficViolationBack)));
        assertThat(driver.getTrafficViolations()).containsOnly(trafficViolationBack);
        assertThat(trafficViolationBack.getDriver()).isEqualTo(driver);

        driver.setTrafficViolations(new HashSet<>());
        assertThat(driver.getTrafficViolations()).doesNotContain(trafficViolationBack);
        assertThat(trafficViolationBack.getDriver()).isNull();
    }

    @Test
    void contractTest() {
        Driver driver = getDriverRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        driver.setContract(contractBack);
        assertThat(driver.getContract()).isEqualTo(contractBack);

        driver.contract(null);
        assertThat(driver.getContract()).isNull();
    }
}
