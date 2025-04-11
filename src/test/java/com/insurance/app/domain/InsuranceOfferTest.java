package com.insurance.app.domain;

import static com.insurance.app.domain.ContractTestSamples.*;
import static com.insurance.app.domain.InsuranceOfferTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceOfferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceOffer.class);
        InsuranceOffer insuranceOffer1 = getInsuranceOfferSample1();
        InsuranceOffer insuranceOffer2 = new InsuranceOffer();
        assertThat(insuranceOffer1).isNotEqualTo(insuranceOffer2);

        insuranceOffer2.setId(insuranceOffer1.getId());
        assertThat(insuranceOffer1).isEqualTo(insuranceOffer2);

        insuranceOffer2 = getInsuranceOfferSample2();
        assertThat(insuranceOffer1).isNotEqualTo(insuranceOffer2);
    }

    @Test
    void contractTest() {
        InsuranceOffer insuranceOffer = getInsuranceOfferRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        insuranceOffer.setContract(contractBack);
        assertThat(insuranceOffer.getContract()).isEqualTo(contractBack);

        insuranceOffer.contract(null);
        assertThat(insuranceOffer.getContract()).isNull();
    }
}
