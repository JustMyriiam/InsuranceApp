package com.insurance.app.domain;

import static com.insurance.app.domain.ContractTestSamples.*;
import static com.insurance.app.domain.DocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Document.class);
        Document document1 = getDocumentSample1();
        Document document2 = new Document();
        assertThat(document1).isNotEqualTo(document2);

        document2.setId(document1.getId());
        assertThat(document1).isEqualTo(document2);

        document2 = getDocumentSample2();
        assertThat(document1).isNotEqualTo(document2);
    }

    @Test
    void contractTest() {
        Document document = getDocumentRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        document.setContract(contractBack);
        assertThat(document.getContract()).isEqualTo(contractBack);

        document.contract(null);
        assertThat(document.getContract()).isNull();
    }
}
