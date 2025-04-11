package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccidentHistory.
 */
@Entity
@Table(name = "accident_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccidentHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "accident_id")
    private String accidentId;

    @Column(name = "accident_date")
    private Instant accidentDate;

    @Column(name = "severity")
    private String severity;

    @Column(name = "description")
    private String description;

    @Column(name = "repair_cost")
    private Double repairCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "cars", "drivers", "insuranceOffers", "documents", "accidentHistories", "parkings", "burntStolenIncidents", "client" },
        allowSetters = true
    )
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "accidentHistories" }, allowSetters = true)
    private DocumentSinister documentSinister;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccidentHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccidentId() {
        return this.accidentId;
    }

    public AccidentHistory accidentId(String accidentId) {
        this.setAccidentId(accidentId);
        return this;
    }

    public void setAccidentId(String accidentId) {
        this.accidentId = accidentId;
    }

    public Instant getAccidentDate() {
        return this.accidentDate;
    }

    public AccidentHistory accidentDate(Instant accidentDate) {
        this.setAccidentDate(accidentDate);
        return this;
    }

    public void setAccidentDate(Instant accidentDate) {
        this.accidentDate = accidentDate;
    }

    public String getSeverity() {
        return this.severity;
    }

    public AccidentHistory severity(String severity) {
        this.setSeverity(severity);
        return this;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return this.description;
    }

    public AccidentHistory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRepairCost() {
        return this.repairCost;
    }

    public AccidentHistory repairCost(Double repairCost) {
        this.setRepairCost(repairCost);
        return this;
    }

    public void setRepairCost(Double repairCost) {
        this.repairCost = repairCost;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public AccidentHistory contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    public DocumentSinister getDocumentSinister() {
        return this.documentSinister;
    }

    public void setDocumentSinister(DocumentSinister documentSinister) {
        this.documentSinister = documentSinister;
    }

    public AccidentHistory documentSinister(DocumentSinister documentSinister) {
        this.setDocumentSinister(documentSinister);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccidentHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((AccidentHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccidentHistory{" +
            "id=" + getId() +
            ", accidentId='" + getAccidentId() + "'" +
            ", accidentDate='" + getAccidentDate() + "'" +
            ", severity='" + getSeverity() + "'" +
            ", description='" + getDescription() + "'" +
            ", repairCost=" + getRepairCost() +
            "}";
    }
}
