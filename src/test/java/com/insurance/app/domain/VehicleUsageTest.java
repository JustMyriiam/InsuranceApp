package com.insurance.app.domain;

import static com.insurance.app.domain.CarTestSamples.*;
import static com.insurance.app.domain.VehicleUsageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleUsageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleUsage.class);
        VehicleUsage vehicleUsage1 = getVehicleUsageSample1();
        VehicleUsage vehicleUsage2 = new VehicleUsage();
        assertThat(vehicleUsage1).isNotEqualTo(vehicleUsage2);

        vehicleUsage2.setId(vehicleUsage1.getId());
        assertThat(vehicleUsage1).isEqualTo(vehicleUsage2);

        vehicleUsage2 = getVehicleUsageSample2();
        assertThat(vehicleUsage1).isNotEqualTo(vehicleUsage2);
    }

    @Test
    void carTest() {
        VehicleUsage vehicleUsage = getVehicleUsageRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        vehicleUsage.setCar(carBack);
        assertThat(vehicleUsage.getCar()).isEqualTo(carBack);

        vehicleUsage.car(null);
        assertThat(vehicleUsage.getCar()).isNull();
    }
}
