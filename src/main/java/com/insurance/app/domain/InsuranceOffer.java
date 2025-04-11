package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InsuranceOffer.
 */
@Entity
@Table(name = "insurance_offer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceOffer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "offer_id")
    private String offerId;

    @Column(name = "offer_name")
    private String offerName;

    @Column(name = "price")
    private Double price;

    @Column(name = "coverage_details")
    private String coverageDetails;

    @Column(name = "terms_and_conditions")
    private String termsAndConditions;

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

    public InsuranceOffer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOfferId() {
        return this.offerId;
    }

    public InsuranceOffer offerId(String offerId) {
        this.setOfferId(offerId);
        return this;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferName() {
        return this.offerName;
    }

    public InsuranceOffer offerName(String offerName) {
        this.setOfferName(offerName);
        return this;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public Double getPrice() {
        return this.price;
    }

    public InsuranceOffer price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCoverageDetails() {
        return this.coverageDetails;
    }

    public InsuranceOffer coverageDetails(String coverageDetails) {
        this.setCoverageDetails(coverageDetails);
        return this;
    }

    public void setCoverageDetails(String coverageDetails) {
        this.coverageDetails = coverageDetails;
    }

    public String getTermsAndConditions() {
        return this.termsAndConditions;
    }

    public InsuranceOffer termsAndConditions(String termsAndConditions) {
        this.setTermsAndConditions(termsAndConditions);
        return this;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public InsuranceOffer contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceOffer)) {
            return false;
        }
        return getId() != null && getId().equals(((InsuranceOffer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceOffer{" +
            "id=" + getId() +
            ", offerId='" + getOfferId() + "'" +
            ", offerName='" + getOfferName() + "'" +
            ", price=" + getPrice() +
            ", coverageDetails='" + getCoverageDetails() + "'" +
            ", termsAndConditions='" + getTermsAndConditions() + "'" +
            "}";
    }
}
