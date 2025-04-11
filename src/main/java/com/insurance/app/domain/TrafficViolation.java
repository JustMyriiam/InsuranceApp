package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrafficViolation.
 */
@Entity
@Table(name = "traffic_violation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrafficViolation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "violation_type")
    private String violationType;

    @Column(name = "violation_date")
    private Instant violationDate;

    @Column(name = "penalty_points")
    private Double penaltyPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trafficViolations", "contract" }, allowSetters = true)
    private Driver driver;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrafficViolation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getViolationType() {
        return this.violationType;
    }

    public TrafficViolation violationType(String violationType) {
        this.setViolationType(violationType);
        return this;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }

    public Instant getViolationDate() {
        return this.violationDate;
    }

    public TrafficViolation violationDate(Instant violationDate) {
        this.setViolationDate(violationDate);
        return this;
    }

    public void setViolationDate(Instant violationDate) {
        this.violationDate = violationDate;
    }

    public Double getPenaltyPoints() {
        return this.penaltyPoints;
    }

    public TrafficViolation penaltyPoints(Double penaltyPoints) {
        this.setPenaltyPoints(penaltyPoints);
        return this;
    }

    public void setPenaltyPoints(Double penaltyPoints) {
        this.penaltyPoints = penaltyPoints;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public TrafficViolation driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrafficViolation)) {
            return false;
        }
        return getId() != null && getId().equals(((TrafficViolation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrafficViolation{" +
            "id=" + getId() +
            ", violationType='" + getViolationType() + "'" +
            ", violationDate='" + getViolationDate() + "'" +
            ", penaltyPoints=" + getPenaltyPoints() +
            "}";
    }
}
