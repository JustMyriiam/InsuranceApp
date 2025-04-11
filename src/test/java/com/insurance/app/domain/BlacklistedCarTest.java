package com.insurance.app.domain;

import static com.insurance.app.domain.BlacklistedCarTestSamples.*;
import static com.insurance.app.domain.CarTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BlacklistedCarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlacklistedCar.class);
        BlacklistedCar blacklistedCar1 = getBlacklistedCarSample1();
        BlacklistedCar blacklistedCar2 = new BlacklistedCar();
        assertThat(blacklistedCar1).isNotEqualTo(blacklistedCar2);

        blacklistedCar2.setId(blacklistedCar1.getId());
        assertThat(blacklistedCar1).isEqualTo(blacklistedCar2);

        blacklistedCar2 = getBlacklistedCarSample2();
        assertThat(blacklistedCar1).isNotEqualTo(blacklistedCar2);
    }

    @Test
    void carTest() {
        BlacklistedCar blacklistedCar = getBlacklistedCarRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        blacklistedCar.setCar(carBack);
        assertThat(blacklistedCar.getCar()).isEqualTo(carBack);

        blacklistedCar.car(null);
        assertThat(blacklistedCar.getCar()).isNull();
    }
}
