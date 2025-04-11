package com.insurance.app.domain;

import static com.insurance.app.domain.CarTestSamples.*;
import static com.insurance.app.domain.VehicleAccessoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleAccessoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleAccessory.class);
        VehicleAccessory vehicleAccessory1 = getVehicleAccessorySample1();
        VehicleAccessory vehicleAccessory2 = new VehicleAccessory();
        assertThat(vehicleAccessory1).isNotEqualTo(vehicleAccessory2);

        vehicleAccessory2.setId(vehicleAccessory1.getId());
        assertThat(vehicleAccessory1).isEqualTo(vehicleAccessory2);

        vehicleAccessory2 = getVehicleAccessorySample2();
        assertThat(vehicleAccessory1).isNotEqualTo(vehicleAccessory2);
    }

    @Test
    void carTest() {
        VehicleAccessory vehicleAccessory = getVehicleAccessoryRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        vehicleAccessory.setCar(carBack);
        assertThat(vehicleAccessory.getCar()).isEqualTo(carBack);

        vehicleAccessory.car(null);
        assertThat(vehicleAccessory.getCar()).isNull();
    }
}
