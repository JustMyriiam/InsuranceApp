package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LocationRisk.
 */
@Entity
@Table(name = "location_risk")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocationRisk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "region")
    private String region;

    @Column(name = "theft_risk")
    private Double theftRisk;

    @Column(name = "accident_risk")
    private Double accidentRisk;

    @Column(name = "weather_risk")
    private Double weatherRisk;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "locationRisk")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "vehicleUsages", "vehicleAccessories", "blacklistedCars", "contract", "locationRisk" },
        allowSetters = true
    )
    private Set<Car> cars = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LocationRisk id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegion() {
        return this.region;
    }

    public LocationRisk region(String region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getTheftRisk() {
        return this.theftRisk;
    }

    public LocationRisk theftRisk(Double theftRisk) {
        this.setTheftRisk(theftRisk);
        return this;
    }

    public void setTheftRisk(Double theftRisk) {
        this.theftRisk = theftRisk;
    }

    public Double getAccidentRisk() {
        return this.accidentRisk;
    }

    public LocationRisk accidentRisk(Double accidentRisk) {
        this.setAccidentRisk(accidentRisk);
        return this;
    }

    public void setAccidentRisk(Double accidentRisk) {
        this.accidentRisk = accidentRisk;
    }

    public Double getWeatherRisk() {
        return this.weatherRisk;
    }

    public LocationRisk weatherRisk(Double weatherRisk) {
        this.setWeatherRisk(weatherRisk);
        return this;
    }

    public void setWeatherRisk(Double weatherRisk) {
        this.weatherRisk = weatherRisk;
    }

    public Set<Car> getCars() {
        return this.cars;
    }

    public void setCars(Set<Car> cars) {
        if (this.cars != null) {
            this.cars.forEach(i -> i.setLocationRisk(null));
        }
        if (cars != null) {
            cars.forEach(i -> i.setLocationRisk(this));
        }
        this.cars = cars;
    }

    public LocationRisk cars(Set<Car> cars) {
        this.setCars(cars);
        return this;
    }

    public LocationRisk addCars(Car car) {
        this.cars.add(car);
        car.setLocationRisk(this);
        return this;
    }

    public LocationRisk removeCars(Car car) {
        this.cars.remove(car);
        car.setLocationRisk(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationRisk)) {
            return false;
        }
        return getId() != null && getId().equals(((LocationRisk) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationRisk{" +
            "id=" + getId() +
            ", region='" + getRegion() + "'" +
            ", theftRisk=" + getTheftRisk() +
            ", accidentRisk=" + getAccidentRisk() +
            ", weatherRisk=" + getWeatherRisk() +
            "}";
    }
}
