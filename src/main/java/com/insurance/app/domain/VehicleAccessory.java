package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VehicleAccessory.
 */
@Entity
@Table(name = "vehicle_accessory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleAccessory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "accessory_id")
    private String accessoryId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "factory_installed")
    private Boolean factoryInstalled;

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

    public VehicleAccessory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessoryId() {
        return this.accessoryId;
    }

    public VehicleAccessory accessoryId(String accessoryId) {
        this.setAccessoryId(accessoryId);
        return this;
    }

    public void setAccessoryId(String accessoryId) {
        this.accessoryId = accessoryId;
    }

    public String getName() {
        return this.name;
    }

    public VehicleAccessory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public VehicleAccessory type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getFactoryInstalled() {
        return this.factoryInstalled;
    }

    public VehicleAccessory factoryInstalled(Boolean factoryInstalled) {
        this.setFactoryInstalled(factoryInstalled);
        return this;
    }

    public void setFactoryInstalled(Boolean factoryInstalled) {
        this.factoryInstalled = factoryInstalled;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public VehicleAccessory car(Car car) {
        this.setCar(car);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleAccessory)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleAccessory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleAccessory{" +
            "id=" + getId() +
            ", accessoryId='" + getAccessoryId() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", factoryInstalled='" + getFactoryInstalled() + "'" +
            "}";
    }
}
