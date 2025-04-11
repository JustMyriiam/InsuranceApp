package com.insurance.app.domain;

import static com.insurance.app.domain.CarTestSamples.*;
import static com.insurance.app.domain.LocationRiskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LocationRiskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationRisk.class);
        LocationRisk locationRisk1 = getLocationRiskSample1();
        LocationRisk locationRisk2 = new LocationRisk();
        assertThat(locationRisk1).isNotEqualTo(locationRisk2);

        locationRisk2.setId(locationRisk1.getId());
        assertThat(locationRisk1).isEqualTo(locationRisk2);

        locationRisk2 = getLocationRiskSample2();
        assertThat(locationRisk1).isNotEqualTo(locationRisk2);
    }

    @Test
    void carsTest() {
        LocationRisk locationRisk = getLocationRiskRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        locationRisk.addCars(carBack);
        assertThat(locationRisk.getCars()).containsOnly(carBack);
        assertThat(carBack.getLocationRisk()).isEqualTo(locationRisk);

        locationRisk.removeCars(carBack);
        assertThat(locationRisk.getCars()).doesNotContain(carBack);
        assertThat(carBack.getLocationRisk()).isNull();

        locationRisk.cars(new HashSet<>(Set.of(carBack)));
        assertThat(locationRisk.getCars()).containsOnly(carBack);
        assertThat(carBack.getLocationRisk()).isEqualTo(locationRisk);

        locationRisk.setCars(new HashSet<>());
        assertThat(locationRisk.getCars()).doesNotContain(carBack);
        assertThat(carBack.getLocationRisk()).isNull();
    }
}
