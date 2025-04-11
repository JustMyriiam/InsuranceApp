package com.insurance.app.domain;

import static com.insurance.app.domain.ClientTestSamples.*;
import static com.insurance.app.domain.ContractTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void contractTest() {
        Client client = getClientRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        client.addContract(contractBack);
        assertThat(client.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getClient()).isEqualTo(client);

        client.removeContract(contractBack);
        assertThat(client.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getClient()).isNull();

        client.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(client.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getClient()).isEqualTo(client);

        client.setContracts(new HashSet<>());
        assertThat(client.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getClient()).isNull();
    }
}
