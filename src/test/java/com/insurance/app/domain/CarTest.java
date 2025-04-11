package com.insurance.app.domain;

import static com.insurance.app.domain.BlacklistedCarTestSamples.*;
import static com.insurance.app.domain.CarTestSamples.*;
import static com.insurance.app.domain.ContractTestSamples.*;
import static com.insurance.app.domain.LocationRiskTestSamples.*;
import static com.insurance.app.domain.VehicleAccessoryTestSamples.*;
import static com.insurance.app.domain.VehicleUsageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Car.class);
        Car car1 = getCarSample1();
        Car car2 = new Car();
        assertThat(car1).isNotEqualTo(car2);

        car2.setId(car1.getId());
        assertThat(car1).isEqualTo(car2);

        car2 = getCarSample2();
        assertThat(car1).isNotEqualTo(car2);
    }

    @Test
    void vehicleUsageTest() {
        Car car = getCarRandomSampleGenerator();
        VehicleUsage vehicleUsageBack = getVehicleUsageRandomSampleGenerator();

        car.addVehicleUsage(vehicleUsageBack);
        assertThat(car.getVehicleUsages()).containsOnly(vehicleUsageBack);
        assertThat(vehicleUsageBack.getCar()).isEqualTo(car);

        car.removeVehicleUsage(vehicleUsageBack);
        assertThat(car.getVehicleUsages()).doesNotContain(vehicleUsageBack);
        assertThat(vehicleUsageBack.getCar()).isNull();

        car.vehicleUsages(new HashSet<>(Set.of(vehicleUsageBack)));
        assertThat(car.getVehicleUsages()).containsOnly(vehicleUsageBack);
        assertThat(vehicleUsageBack.getCar()).isEqualTo(car);

        car.setVehicleUsages(new HashSet<>());
        assertThat(car.getVehicleUsages()).doesNotContain(vehicleUsageBack);
        assertThat(vehicleUsageBack.getCar()).isNull();
    }

    @Test
    void vehicleAccessoryTest() {
        Car car = getCarRandomSampleGenerator();
        VehicleAccessory vehicleAccessoryBack = getVehicleAccessoryRandomSampleGenerator();

        car.addVehicleAccessory(vehicleAccessoryBack);
        assertThat(car.getVehicleAccessories()).containsOnly(vehicleAccessoryBack);
        assertThat(vehicleAccessoryBack.getCar()).isEqualTo(car);

        car.removeVehicleAccessory(vehicleAccessoryBack);
        assertThat(car.getVehicleAccessories()).doesNotContain(vehicleAccessoryBack);
        assertThat(vehicleAccessoryBack.getCar()).isNull();

        car.vehicleAccessories(new HashSet<>(Set.of(vehicleAccessoryBack)));
        assertThat(car.getVehicleAccessories()).containsOnly(vehicleAccessoryBack);
        assertThat(vehicleAccessoryBack.getCar()).isEqualTo(car);

        car.setVehicleAccessories(new HashSet<>());
        assertThat(car.getVehicleAccessories()).doesNotContain(vehicleAccessoryBack);
        assertThat(vehicleAccessoryBack.getCar()).isNull();
    }

    @Test
    void blacklistedCarTest() {
        Car car = getCarRandomSampleGenerator();
        BlacklistedCar blacklistedCarBack = getBlacklistedCarRandomSampleGenerator();

        car.addBlacklistedCar(blacklistedCarBack);
        assertThat(car.getBlacklistedCars()).containsOnly(blacklistedCarBack);
        assertThat(blacklistedCarBack.getCar()).isEqualTo(car);

        car.removeBlacklistedCar(blacklistedCarBack);
        assertThat(car.getBlacklistedCars()).doesNotContain(blacklistedCarBack);
        assertThat(blacklistedCarBack.getCar()).isNull();

        car.blacklistedCars(new HashSet<>(Set.of(blacklistedCarBack)));
        assertThat(car.getBlacklistedCars()).containsOnly(blacklistedCarBack);
        assertThat(blacklistedCarBack.getCar()).isEqualTo(car);

        car.setBlacklistedCars(new HashSet<>());
        assertThat(car.getBlacklistedCars()).doesNotContain(blacklistedCarBack);
        assertThat(blacklistedCarBack.getCar()).isNull();
    }

    @Test
    void contractTest() {
        Car car = getCarRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        car.setContract(contractBack);
        assertThat(car.getContract()).isEqualTo(contractBack);

        car.contract(null);
        assertThat(car.getContract()).isNull();
    }

    @Test
    void locationRiskTest() {
        Car car = getCarRandomSampleGenerator();
        LocationRisk locationRiskBack = getLocationRiskRandomSampleGenerator();

        car.setLocationRisk(locationRiskBack);
        assertThat(car.getLocationRisk()).isEqualTo(locationRiskBack);

        car.locationRisk(null);
        assertThat(car.getLocationRisk()).isNull();
    }
}
