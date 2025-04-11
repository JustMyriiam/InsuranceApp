package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Parking.
 */
@Entity
@Table(name = "parking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Parking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "parking_id")
    private String parkingId;

    @Column(name = "location")
    private String location;

    @Column(name = "is_secured")
    private Boolean isSecured;

    @Column(name = "capacity")
    private Integer capacity;

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

    public Parking id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParkingId() {
        return this.parkingId;
    }

    public Parking parkingId(String parkingId) {
        this.setParkingId(parkingId);
        return this;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getLocation() {
        return this.location;
    }

    public Parking location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getIsSecured() {
        return this.isSecured;
    }

    public Parking isSecured(Boolean isSecured) {
        this.setIsSecured(isSecured);
        return this;
    }

    public void setIsSecured(Boolean isSecured) {
        this.isSecured = isSecured;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public Parking capacity(Integer capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Parking contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parking)) {
            return false;
        }
        return getId() != null && getId().equals(((Parking) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parking{" +
            "id=" + getId() +
            ", parkingId='" + getParkingId() + "'" +
            ", location='" + getLocation() + "'" +
            ", isSecured='" + getIsSecured() + "'" +
            ", capacity=" + getCapacity() +
            "}";
    }
}
