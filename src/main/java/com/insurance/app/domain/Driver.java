package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Driver.
 */
@Entity
@Table(name = "driver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "license_category")
    private String licenseCategory;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "accident_history")
    private String accidentHistory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "driver" }, allowSetters = true)
    private Set<TrafficViolation> trafficViolations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "cars", "drivers", "insuranceOffers", "documents", "accidentHistories", "parkings", "burntStolenIncidents", "client" },
        allowSetters = true
    )
    private Contract contract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Driver id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Driver fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Driver dateOfBirth(String dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLicenseNumber() {
        return this.licenseNumber;
    }

    public Driver licenseNumber(String licenseNumber) {
        this.setLicenseNumber(licenseNumber);
        return this;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseCategory() {
        return this.licenseCategory;
    }

    public Driver licenseCategory(String licenseCategory) {
        this.setLicenseCategory(licenseCategory);
        return this;
    }

    public void setLicenseCategory(String licenseCategory) {
        this.licenseCategory = licenseCategory;
    }

    public String getAddress() {
        return this.address;
    }

    public Driver address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Driver phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getYearsOfExperience() {
        return this.yearsOfExperience;
    }

    public Driver yearsOfExperience(Integer yearsOfExperience) {
        this.setYearsOfExperience(yearsOfExperience);
        return this;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getAccidentHistory() {
        return this.accidentHistory;
    }

    public Driver accidentHistory(String accidentHistory) {
        this.setAccidentHistory(accidentHistory);
        return this;
    }

    public void setAccidentHistory(String accidentHistory) {
        this.accidentHistory = accidentHistory;
    }

    public Set<TrafficViolation> getTrafficViolations() {
        return this.trafficViolations;
    }

    public void setTrafficViolations(Set<TrafficViolation> trafficViolations) {
        if (this.trafficViolations != null) {
            this.trafficViolations.forEach(i -> i.setDriver(null));
        }
        if (trafficViolations != null) {
            trafficViolations.forEach(i -> i.setDriver(this));
        }
        this.trafficViolations = trafficViolations;
    }

    public Driver trafficViolations(Set<TrafficViolation> trafficViolations) {
        this.setTrafficViolations(trafficViolations);
        return this;
    }

    public Driver addTrafficViolation(TrafficViolation trafficViolation) {
        this.trafficViolations.add(trafficViolation);
        trafficViolation.setDriver(this);
        return this;
    }

    public Driver removeTrafficViolation(TrafficViolation trafficViolation) {
        this.trafficViolations.remove(trafficViolation);
        trafficViolation.setDriver(null);
        return this;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Driver contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Driver)) {
            return false;
        }
        return getId() != null && getId().equals(((Driver) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Driver{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", licenseNumber='" + getLicenseNumber() + "'" +
            ", licenseCategory='" + getLicenseCategory() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", yearsOfExperience=" + getYearsOfExperience() +
            ", accidentHistory='" + getAccidentHistory() + "'" +
            "}";
    }
}
