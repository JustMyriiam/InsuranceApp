package com.insurance.app.domain;

import static com.insurance.app.domain.BurntStolenIncidentTestSamples.*;
import static com.insurance.app.domain.ContractTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BurntStolenIncidentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BurntStolenIncident.class);
        BurntStolenIncident burntStolenIncident1 = getBurntStolenIncidentSample1();
        BurntStolenIncident burntStolenIncident2 = new BurntStolenIncident();
        assertThat(burntStolenIncident1).isNotEqualTo(burntStolenIncident2);

        burntStolenIncident2.setId(burntStolenIncident1.getId());
        assertThat(burntStolenIncident1).isEqualTo(burntStolenIncident2);

        burntStolenIncident2 = getBurntStolenIncidentSample2();
        assertThat(burntStolenIncident1).isNotEqualTo(burntStolenIncident2);
    }

    @Test
    void contractTest() {
        BurntStolenIncident burntStolenIncident = getBurntStolenIncidentRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        burntStolenIncident.setContract(contractBack);
        assertThat(burntStolenIncident.getContract()).isEqualTo(contractBack);

        burntStolenIncident.contract(null);
        assertThat(burntStolenIncident.getContract()).isNull();
    }
}
