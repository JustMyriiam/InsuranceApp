package com.insurance.app.domain;

import static com.insurance.app.domain.ContractTestSamples.*;
import static com.insurance.app.domain.ParkingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParkingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parking.class);
        Parking parking1 = getParkingSample1();
        Parking parking2 = new Parking();
        assertThat(parking1).isNotEqualTo(parking2);

        parking2.setId(parking1.getId());
        assertThat(parking1).isEqualTo(parking2);

        parking2 = getParkingSample2();
        assertThat(parking1).isNotEqualTo(parking2);
    }

    @Test
    void contractTest() {
        Parking parking = getParkingRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        parking.setContract(contractBack);
        assertThat(parking.getContract()).isEqualTo(contractBack);

        parking.contract(null);
        assertThat(parking.getContract()).isNull();
    }
}
