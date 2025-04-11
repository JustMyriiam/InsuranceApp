package com.insurance.app.domain;

import static com.insurance.app.domain.AccidentHistoryTestSamples.*;
import static com.insurance.app.domain.BurntStolenIncidentTestSamples.*;
import static com.insurance.app.domain.CarTestSamples.*;
import static com.insurance.app.domain.ClientTestSamples.*;
import static com.insurance.app.domain.ContractTestSamples.*;
import static com.insurance.app.domain.DocumentTestSamples.*;
import static com.insurance.app.domain.DriverTestSamples.*;
import static com.insurance.app.domain.InsuranceOfferTestSamples.*;
import static com.insurance.app.domain.ParkingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = getContractSample1();
        Contract contract2 = new Contract();
        assertThat(contract1).isNotEqualTo(contract2);

        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);

        contract2 = getContractSample2();
        assertThat(contract1).isNotEqualTo(contract2);
    }

    @Test
    void carTest() {
        Contract contract = getContractRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        contract.addCar(carBack);
        assertThat(contract.getCars()).containsOnly(carBack);
        assertThat(carBack.getContract()).isEqualTo(contract);

        contract.removeCar(carBack);
        assertThat(contract.getCars()).doesNotContain(carBack);
        assertThat(carBack.getContract()).isNull();

        contract.cars(new HashSet<>(Set.of(carBack)));
        assertThat(contract.getCars()).containsOnly(carBack);
        assertThat(carBack.getContract()).isEqualTo(contract);

        contract.setCars(new HashSet<>());
        assertThat(contract.getCars()).doesNotContain(carBack);
        assertThat(carBack.getContract()).isNull();
    }

    @Test
    void driverTest() {
        Contract contract = getContractRandomSampleGenerator();
        Driver driverBack = getDriverRandomSampleGenerator();

        contract.addDriver(driverBack);
        assertThat(contract.getDrivers()).containsOnly(driverBack);
        assertThat(driverBack.getContract()).isEqualTo(contract);

        contract.removeDriver(driverBack);
        assertThat(contract.getDrivers()).doesNotContain(driverBack);
        assertThat(driverBack.getContract()).isNull();

        contract.drivers(new HashSet<>(Set.of(driverBack)));
        assertThat(contract.getDrivers()).containsOnly(driverBack);
        assertThat(driverBack.getContract()).isEqualTo(contract);

        contract.setDrivers(new HashSet<>());
        assertThat(contract.getDrivers()).doesNotContain(driverBack);
        assertThat(driverBack.getContract()).isNull();
    }

    @Test
    void insuranceOfferTest() {
        Contract contract = getContractRandomSampleGenerator();
        InsuranceOffer insuranceOfferBack = getInsuranceOfferRandomSampleGenerator();

        contract.addInsuranceOffer(insuranceOfferBack);
        assertThat(contract.getInsuranceOffers()).containsOnly(insuranceOfferBack);
        assertThat(insuranceOfferBack.getContract()).isEqualTo(contract);

        contract.removeInsuranceOffer(insuranceOfferBack);
        assertThat(contract.getInsuranceOffers()).doesNotContain(insuranceOfferBack);
        assertThat(insuranceOfferBack.getContract()).isNull();

        contract.insuranceOffers(new HashSet<>(Set.of(insuranceOfferBack)));
        assertThat(contract.getInsuranceOffers()).containsOnly(insuranceOfferBack);
        assertThat(insuranceOfferBack.getContract()).isEqualTo(contract);

        contract.setInsuranceOffers(new HashSet<>());
        assertThat(contract.getInsuranceOffers()).doesNotContain(insuranceOfferBack);
        assertThat(insuranceOfferBack.getContract()).isNull();
    }

    @Test
    void documentTest() {
        Contract contract = getContractRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        contract.addDocument(documentBack);
        assertThat(contract.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getContract()).isEqualTo(contract);

        contract.removeDocument(documentBack);
        assertThat(contract.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getContract()).isNull();

        contract.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(contract.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getContract()).isEqualTo(contract);

        contract.setDocuments(new HashSet<>());
        assertThat(contract.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getContract()).isNull();
    }

    @Test
    void accidentHistoryTest() {
        Contract contract = getContractRandomSampleGenerator();
        AccidentHistory accidentHistoryBack = getAccidentHistoryRandomSampleGenerator();

        contract.addAccidentHistory(accidentHistoryBack);
        assertThat(contract.getAccidentHistories()).containsOnly(accidentHistoryBack);
        assertThat(accidentHistoryBack.getContract()).isEqualTo(contract);

        contract.removeAccidentHistory(accidentHistoryBack);
        assertThat(contract.getAccidentHistories()).doesNotContain(accidentHistoryBack);
        assertThat(accidentHistoryBack.getContract()).isNull();

        contract.accidentHistories(new HashSet<>(Set.of(accidentHistoryBack)));
        assertThat(contract.getAccidentHistories()).containsOnly(accidentHistoryBack);
        assertThat(accidentHistoryBack.getContract()).isEqualTo(contract);

        contract.setAccidentHistories(new HashSet<>());
        assertThat(contract.getAccidentHistories()).doesNotContain(accidentHistoryBack);
        assertThat(accidentHistoryBack.getContract()).isNull();
    }

    @Test
    void parkingTest() {
        Contract contract = getContractRandomSampleGenerator();
        Parking parkingBack = getParkingRandomSampleGenerator();

        contract.addParking(parkingBack);
        assertThat(contract.getParkings()).containsOnly(parkingBack);
        assertThat(parkingBack.getContract()).isEqualTo(contract);

        contract.removeParking(parkingBack);
        assertThat(contract.getParkings()).doesNotContain(parkingBack);
        assertThat(parkingBack.getContract()).isNull();

        contract.parkings(new HashSet<>(Set.of(parkingBack)));
        assertThat(contract.getParkings()).containsOnly(parkingBack);
        assertThat(parkingBack.getContract()).isEqualTo(contract);

        contract.setParkings(new HashSet<>());
        assertThat(contract.getParkings()).doesNotContain(parkingBack);
        assertThat(parkingBack.getContract()).isNull();
    }

    @Test
    void burntStolenIncidentTest() {
        Contract contract = getContractRandomSampleGenerator();
        BurntStolenIncident burntStolenIncidentBack = getBurntStolenIncidentRandomSampleGenerator();

        contract.addBurntStolenIncident(burntStolenIncidentBack);
        assertThat(contract.getBurntStolenIncidents()).containsOnly(burntStolenIncidentBack);
        assertThat(burntStolenIncidentBack.getContract()).isEqualTo(contract);

        contract.removeBurntStolenIncident(burntStolenIncidentBack);
        assertThat(contract.getBurntStolenIncidents()).doesNotContain(burntStolenIncidentBack);
        assertThat(burntStolenIncidentBack.getContract()).isNull();

        contract.burntStolenIncidents(new HashSet<>(Set.of(burntStolenIncidentBack)));
        assertThat(contract.getBurntStolenIncidents()).containsOnly(burntStolenIncidentBack);
        assertThat(burntStolenIncidentBack.getContract()).isEqualTo(contract);

        contract.setBurntStolenIncidents(new HashSet<>());
        assertThat(contract.getBurntStolenIncidents()).doesNotContain(burntStolenIncidentBack);
        assertThat(burntStolenIncidentBack.getContract()).isNull();
    }

    @Test
    void clientTest() {
        Contract contract = getContractRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        contract.setClient(clientBack);
        assertThat(contract.getClient()).isEqualTo(clientBack);

        contract.client(null);
        assertThat(contract.getClient()).isNull();
    }
}
