package com.insurance.app.domain;

import static com.insurance.app.domain.AccidentHistoryTestSamples.*;
import static com.insurance.app.domain.ContractTestSamples.*;
import static com.insurance.app.domain.DocumentSinisterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccidentHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccidentHistory.class);
        AccidentHistory accidentHistory1 = getAccidentHistorySample1();
        AccidentHistory accidentHistory2 = new AccidentHistory();
        assertThat(accidentHistory1).isNotEqualTo(accidentHistory2);

        accidentHistory2.setId(accidentHistory1.getId());
        assertThat(accidentHistory1).isEqualTo(accidentHistory2);

        accidentHistory2 = getAccidentHistorySample2();
        assertThat(accidentHistory1).isNotEqualTo(accidentHistory2);
    }

    @Test
    void contractTest() {
        AccidentHistory accidentHistory = getAccidentHistoryRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        accidentHistory.setContract(contractBack);
        assertThat(accidentHistory.getContract()).isEqualTo(contractBack);

        accidentHistory.contract(null);
        assertThat(accidentHistory.getContract()).isNull();
    }

    @Test
    void documentSinisterTest() {
        AccidentHistory accidentHistory = getAccidentHistoryRandomSampleGenerator();
        DocumentSinister documentSinisterBack = getDocumentSinisterRandomSampleGenerator();

        accidentHistory.setDocumentSinister(documentSinisterBack);
        assertThat(accidentHistory.getDocumentSinister()).isEqualTo(documentSinisterBack);

        accidentHistory.documentSinister(null);
        assertThat(accidentHistory.getDocumentSinister()).isNull();
    }
}
