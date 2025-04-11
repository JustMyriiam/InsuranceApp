package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "contract_id")
    private String contractId;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "premium_amount")
    private Double premiumAmount;

    @Column(name = "coverage_details")
    private String coverageDetails;

    @Column(name = "status")
    private String status;

    @Column(name = "renouvelable")
    private Boolean renouvelable;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "vehicleUsages", "vehicleAccessories", "blacklistedCars", "contract", "locationRisk" },
        allowSetters = true
    )
    private Set<Car> cars = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trafficViolations", "contract" }, allowSetters = true)
    private Set<Driver> drivers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract" }, allowSetters = true)
    private Set<InsuranceOffer> insuranceOffers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract", "documentSinister" }, allowSetters = true)
    private Set<AccidentHistory> accidentHistories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract" }, allowSetters = true)
    private Set<Parking> parkings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract" }, allowSetters = true)
    private Set<BurntStolenIncident> burntStolenIncidents = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contracts" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractId() {
        return this.contractId;
    }

    public Contract contractId(String contractId) {
        this.setContractId(contractId);
        return this;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Contract startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Contract endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Double getPremiumAmount() {
        return this.premiumAmount;
    }

    public Contract premiumAmount(Double premiumAmount) {
        this.setPremiumAmount(premiumAmount);
        return this;
    }

    public void setPremiumAmount(Double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getCoverageDetails() {
        return this.coverageDetails;
    }

    public Contract coverageDetails(String coverageDetails) {
        this.setCoverageDetails(coverageDetails);
        return this;
    }

    public void setCoverageDetails(String coverageDetails) {
        this.coverageDetails = coverageDetails;
    }

    public String getStatus() {
        return this.status;
    }

    public Contract status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getRenouvelable() {
        return this.renouvelable;
    }

    public Contract renouvelable(Boolean renouvelable) {
        this.setRenouvelable(renouvelable);
        return this;
    }

    public void setRenouvelable(Boolean renouvelable) {
        this.renouvelable = renouvelable;
    }

    public Set<Car> getCars() {
        return this.cars;
    }

    public void setCars(Set<Car> cars) {
        if (this.cars != null) {
            this.cars.forEach(i -> i.setContract(null));
        }
        if (cars != null) {
            cars.forEach(i -> i.setContract(this));
        }
        this.cars = cars;
    }

    public Contract cars(Set<Car> cars) {
        this.setCars(cars);
        return this;
    }

    public Contract addCar(Car car) {
        this.cars.add(car);
        car.setContract(this);
        return this;
    }

    public Contract removeCar(Car car) {
        this.cars.remove(car);
        car.setContract(null);
        return this;
    }

    public Set<Driver> getDrivers() {
        return this.drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
        if (this.drivers != null) {
            this.drivers.forEach(i -> i.setContract(null));
        }
        if (drivers != null) {
            drivers.forEach(i -> i.setContract(this));
        }
        this.drivers = drivers;
    }

    public Contract drivers(Set<Driver> drivers) {
        this.setDrivers(drivers);
        return this;
    }

    public Contract addDriver(Driver driver) {
        this.drivers.add(driver);
        driver.setContract(this);
        return this;
    }

    public Contract removeDriver(Driver driver) {
        this.drivers.remove(driver);
        driver.setContract(null);
        return this;
    }

    public Set<InsuranceOffer> getInsuranceOffers() {
        return this.insuranceOffers;
    }

    public void setInsuranceOffers(Set<InsuranceOffer> insuranceOffers) {
        if (this.insuranceOffers != null) {
            this.insuranceOffers.forEach(i -> i.setContract(null));
        }
        if (insuranceOffers != null) {
            insuranceOffers.forEach(i -> i.setContract(this));
        }
        this.insuranceOffers = insuranceOffers;
    }

    public Contract insuranceOffers(Set<InsuranceOffer> insuranceOffers) {
        this.setInsuranceOffers(insuranceOffers);
        return this;
    }

    public Contract addInsuranceOffer(InsuranceOffer insuranceOffer) {
        this.insuranceOffers.add(insuranceOffer);
        insuranceOffer.setContract(this);
        return this;
    }

    public Contract removeInsuranceOffer(InsuranceOffer insuranceOffer) {
        this.insuranceOffers.remove(insuranceOffer);
        insuranceOffer.setContract(null);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setContract(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setContract(this));
        }
        this.documents = documents;
    }

    public Contract documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Contract addDocument(Document document) {
        this.documents.add(document);
        document.setContract(this);
        return this;
    }

    public Contract removeDocument(Document document) {
        this.documents.remove(document);
        document.setContract(null);
        return this;
    }

    public Set<AccidentHistory> getAccidentHistories() {
        return this.accidentHistories;
    }

    public void setAccidentHistories(Set<AccidentHistory> accidentHistories) {
        if (this.accidentHistories != null) {
            this.accidentHistories.forEach(i -> i.setContract(null));
        }
        if (accidentHistories != null) {
            accidentHistories.forEach(i -> i.setContract(this));
        }
        this.accidentHistories = accidentHistories;
    }

    public Contract accidentHistories(Set<AccidentHistory> accidentHistories) {
        this.setAccidentHistories(accidentHistories);
        return this;
    }

    public Contract addAccidentHistory(AccidentHistory accidentHistory) {
        this.accidentHistories.add(accidentHistory);
        accidentHistory.setContract(this);
        return this;
    }

    public Contract removeAccidentHistory(AccidentHistory accidentHistory) {
        this.accidentHistories.remove(accidentHistory);
        accidentHistory.setContract(null);
        return this;
    }

    public Set<Parking> getParkings() {
        return this.parkings;
    }

    public void setParkings(Set<Parking> parkings) {
        if (this.parkings != null) {
            this.parkings.forEach(i -> i.setContract(null));
        }
        if (parkings != null) {
            parkings.forEach(i -> i.setContract(this));
        }
        this.parkings = parkings;
    }

    public Contract parkings(Set<Parking> parkings) {
        this.setParkings(parkings);
        return this;
    }

    public Contract addParking(Parking parking) {
        this.parkings.add(parking);
        parking.setContract(this);
        return this;
    }

    public Contract removeParking(Parking parking) {
        this.parkings.remove(parking);
        parking.setContract(null);
        return this;
    }

    public Set<BurntStolenIncident> getBurntStolenIncidents() {
        return this.burntStolenIncidents;
    }

    public void setBurntStolenIncidents(Set<BurntStolenIncident> burntStolenIncidents) {
        if (this.burntStolenIncidents != null) {
            this.burntStolenIncidents.forEach(i -> i.setContract(null));
        }
        if (burntStolenIncidents != null) {
            burntStolenIncidents.forEach(i -> i.setContract(this));
        }
        this.burntStolenIncidents = burntStolenIncidents;
    }

    public Contract burntStolenIncidents(Set<BurntStolenIncident> burntStolenIncidents) {
        this.setBurntStolenIncidents(burntStolenIncidents);
        return this;
    }

    public Contract addBurntStolenIncident(BurntStolenIncident burntStolenIncident) {
        this.burntStolenIncidents.add(burntStolenIncident);
        burntStolenIncident.setContract(this);
        return this;
    }

    public Contract removeBurntStolenIncident(BurntStolenIncident burntStolenIncident) {
        this.burntStolenIncidents.remove(burntStolenIncident);
        burntStolenIncident.setContract(null);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Contract client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return getId() != null && getId().equals(((Contract) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", contractId='" + getContractId() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", premiumAmount=" + getPremiumAmount() +
            ", coverageDetails='" + getCoverageDetails() + "'" +
            ", status='" + getStatus() + "'" +
            ", renouvelable='" + getRenouvelable() + "'" +
            "}";
    }
}
