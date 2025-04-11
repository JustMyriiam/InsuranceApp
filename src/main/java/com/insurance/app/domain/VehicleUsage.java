package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VehicleUsage.
 */
@Entity
@Table(name = "vehicle_usage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleUsage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "usage_type")
    private String usageType;

    @Column(name = "annual_mileage")
    private Integer annualMileage;

    @Column(name = "commercial_use")
    private Boolean commercialUse;

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

    public VehicleUsage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsageType() {
        return this.usageType;
    }

    public VehicleUsage usageType(String usageType) {
        this.setUsageType(usageType);
        return this;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public Integer getAnnualMileage() {
        return this.annualMileage;
    }

    public VehicleUsage annualMileage(Integer annualMileage) {
        this.setAnnualMileage(annualMileage);
        return this;
    }

    public void setAnnualMileage(Integer annualMileage) {
        this.annualMileage = annualMileage;
    }

    public Boolean getCommercialUse() {
        return this.commercialUse;
    }

    public VehicleUsage commercialUse(Boolean commercialUse) {
        this.setCommercialUse(commercialUse);
        return this;
    }

    public void setCommercialUse(Boolean commercialUse) {
        this.commercialUse = commercialUse;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public VehicleUsage car(Car car) {
        this.setCar(car);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleUsage)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleUsage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleUsage{" +
            "id=" + getId() +
            ", usageType='" + getUsageType() + "'" +
            ", annualMileage=" + getAnnualMileage() +
            ", commercialUse='" + getCommercialUse() + "'" +
            "}";
    }
}
