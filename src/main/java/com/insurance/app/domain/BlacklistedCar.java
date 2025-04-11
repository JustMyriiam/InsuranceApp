package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BlacklistedCar.
 */
@Entity
@Table(name = "blacklisted_car")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BlacklistedCar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "reason")
    private String reason;

    @Column(name = "blacklist_date")
    private Instant blacklistDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "vehicleUsages", "vehicleAccessories", "blacklistedCars", "contract", "locationRisk" },
        allowSetters = true
    )
    private Car car;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BlacklistedCar id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return this.reason;
    }

    public BlacklistedCar reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getBlacklistDate() {
        return this.blacklistDate;
    }

    public BlacklistedCar blacklistDate(Instant blacklistDate) {
        this.setBlacklistDate(blacklistDate);
        return this;
    }

    public void setBlacklistDate(Instant blacklistDate) {
        this.blacklistDate = blacklistDate;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public BlacklistedCar car(Car car) {
        this.setCar(car);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlacklistedCar)) {
            return false;
        }
        return getId() != null && getId().equals(((BlacklistedCar) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlacklistedCar{" +
            "id=" + getId() +
            ", reason='" + getReason() + "'" +
            ", blacklistDate='" + getBlacklistDate() + "'" +
            "}";
    }
}
