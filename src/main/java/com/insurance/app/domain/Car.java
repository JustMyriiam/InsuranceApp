package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "year")
    private String year;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "transmission")
    private String transmission;

    @Column(name = "engine_size")
    private Double engineSize;

    @Column(name = "color")
    private String color;

    @Column(name = "mileage")
    private Integer mileage;

    @Column(name = "insurance_status")
    private String insuranceStatus;

    @Column(name = "car_type")
    private String carType;

    @Column(name = "is_blacklisted")
    private Boolean isBlacklisted;

    @Column(name = "price_when_bought")
    private Double priceWhenBought;

    @Column(name = "current_price")
    private Double currentPrice;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "car" }, allowSetters = true)
    private Set<VehicleUsage> vehicleUsages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "car" }, allowSetters = true)
    private Set<VehicleAccessory> vehicleAccessories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "car" }, allowSetters = true)
    private Set<BlacklistedCar> blacklistedCars = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "cars", "drivers", "insuranceOffers", "documents", "accidentHistories", "parkings", "burntStolenIncidents", "client" },
        allowSetters = true
    )
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cars" }, allowSetters = true)
    private LocationRisk locationRisk;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Car id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return this.brand;
    }

    public Car brand(String brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public Car model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return this.year;
    }

    public Car year(String year) {
        this.setYear(year);
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public Car registrationNumber(String registrationNumber) {
        this.setRegistrationNumber(registrationNumber);
        return this;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getFuelType() {
        return this.fuelType;
    }

    public Car fuelType(String fuelType) {
        this.setFuelType(fuelType);
        return this;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return this.transmission;
    }

    public Car transmission(String transmission) {
        this.setTransmission(transmission);
        return this;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public Double getEngineSize() {
        return this.engineSize;
    }

    public Car engineSize(Double engineSize) {
        this.setEngineSize(engineSize);
        return this;
    }

    public void setEngineSize(Double engineSize) {
        this.engineSize = engineSize;
    }

    public String getColor() {
        return this.color;
    }

    public Car color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getMileage() {
        return this.mileage;
    }

    public Car mileage(Integer mileage) {
        this.setMileage(mileage);
        return this;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getInsuranceStatus() {
        return this.insuranceStatus;
    }

    public Car insuranceStatus(String insuranceStatus) {
        this.setInsuranceStatus(insuranceStatus);
        return this;
    }

    public void setInsuranceStatus(String insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }

    public String getCarType() {
        return this.carType;
    }

    public Car carType(String carType) {
        this.setCarType(carType);
        return this;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public Boolean getIsBlacklisted() {
        return this.isBlacklisted;
    }

    public Car isBlacklisted(Boolean isBlacklisted) {
        this.setIsBlacklisted(isBlacklisted);
        return this;
    }

    public void setIsBlacklisted(Boolean isBlacklisted) {
        this.isBlacklisted = isBlacklisted;
    }

    public Double getPriceWhenBought() {
        return this.priceWhenBought;
    }

    public Car priceWhenBought(Double priceWhenBought) {
        this.setPriceWhenBought(priceWhenBought);
        return this;
    }

    public void setPriceWhenBought(Double priceWhenBought) {
        this.priceWhenBought = priceWhenBought;
    }

    public Double getCurrentPrice() {
        return this.currentPrice;
    }

    public Car currentPrice(Double currentPrice) {
        this.setCurrentPrice(currentPrice);
        return this;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Set<VehicleUsage> getVehicleUsages() {
        return this.vehicleUsages;
    }

    public void setVehicleUsages(Set<VehicleUsage> vehicleUsages) {
        if (this.vehicleUsages != null) {
            this.vehicleUsages.forEach(i -> i.setCar(null));
        }
        if (vehicleUsages != null) {
            vehicleUsages.forEach(i -> i.setCar(this));
        }
        this.vehicleUsages = vehicleUsages;
    }

    public Car vehicleUsages(Set<VehicleUsage> vehicleUsages) {
        this.setVehicleUsages(vehicleUsages);
        return this;
    }

    public Car addVehicleUsage(VehicleUsage vehicleUsage) {
        this.vehicleUsages.add(vehicleUsage);
        vehicleUsage.setCar(this);
        return this;
    }

    public Car removeVehicleUsage(VehicleUsage vehicleUsage) {
        this.vehicleUsages.remove(vehicleUsage);
        vehicleUsage.setCar(null);
        return this;
    }

    public Set<VehicleAccessory> getVehicleAccessories() {
        return this.vehicleAccessories;
    }

    public void setVehicleAccessories(Set<VehicleAccessory> vehicleAccessories) {
        if (this.vehicleAccessories != null) {
            this.vehicleAccessories.forEach(i -> i.setCar(null));
        }
        if (vehicleAccessories != null) {
            vehicleAccessories.forEach(i -> i.setCar(this));
        }
        this.vehicleAccessories = vehicleAccessories;
    }

    public Car vehicleAccessories(Set<VehicleAccessory> vehicleAccessories) {
        this.setVehicleAccessories(vehicleAccessories);
        return this;
    }

    public Car addVehicleAccessory(VehicleAccessory vehicleAccessory) {
        this.vehicleAccessories.add(vehicleAccessory);
        vehicleAccessory.setCar(this);
        return this;
    }

    public Car removeVehicleAccessory(VehicleAccessory vehicleAccessory) {
        this.vehicleAccessories.remove(vehicleAccessory);
        vehicleAccessory.setCar(null);
        return this;
    }

    public Set<BlacklistedCar> getBlacklistedCars() {
        return this.blacklistedCars;
    }

    public void setBlacklistedCars(Set<BlacklistedCar> blacklistedCars) {
        if (this.blacklistedCars != null) {
            this.blacklistedCars.forEach(i -> i.setCar(null));
        }
        if (blacklistedCars != null) {
            blacklistedCars.forEach(i -> i.setCar(this));
        }
        this.blacklistedCars = blacklistedCars;
    }

    public Car blacklistedCars(Set<BlacklistedCar> blacklistedCars) {
        this.setBlacklistedCars(blacklistedCars);
        return this;
    }

    public Car addBlacklistedCar(BlacklistedCar blacklistedCar) {
        this.blacklistedCars.add(blacklistedCar);
        blacklistedCar.setCar(this);
        return this;
    }

    public Car removeBlacklistedCar(BlacklistedCar blacklistedCar) {
        this.blacklistedCars.remove(blacklistedCar);
        blacklistedCar.setCar(null);
        return this;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Car contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    public LocationRisk getLocationRisk() {
        return this.locationRisk;
    }

    public void setLocationRisk(LocationRisk locationRisk) {
        this.locationRisk = locationRisk;
    }

    public Car locationRisk(LocationRisk locationRisk) {
        this.setLocationRisk(locationRisk);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return getId() != null && getId().equals(((Car) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", brand='" + getBrand() + "'" +
            ", model='" + getModel() + "'" +
            ", year='" + getYear() + "'" +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            ", fuelType='" + getFuelType() + "'" +
            ", transmission='" + getTransmission() + "'" +
            ", engineSize=" + getEngineSize() +
            ", color='" + getColor() + "'" +
            ", mileage=" + getMileage() +
            ", insuranceStatus='" + getInsuranceStatus() + "'" +
            ", carType='" + getCarType() + "'" +
            ", isBlacklisted='" + getIsBlacklisted() + "'" +
            ", priceWhenBought=" + getPriceWhenBought() +
            ", currentPrice=" + getCurrentPrice() +
            "}";
    }
}
