package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BurntStolenIncident.
 */
@Entity
@Table(name = "burnt_stolen_incident")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BurntStolenIncident implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "incident_id")
    private String incidentId;

    @Column(name = "incident_date")
    private Instant incidentDate;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "estimated_loss")
    private Double estimatedLoss;

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

    public BurntStolenIncident id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIncidentId() {
        return this.incidentId;
    }

    public BurntStolenIncident incidentId(String incidentId) {
        this.setIncidentId(incidentId);
        return this;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    public Instant getIncidentDate() {
        return this.incidentDate;
    }

    public BurntStolenIncident incidentDate(Instant incidentDate) {
        this.setIncidentDate(incidentDate);
        return this;
    }

    public void setIncidentDate(Instant incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getType() {
        return this.type;
    }

    public BurntStolenIncident type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public BurntStolenIncident description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getEstimatedLoss() {
        return this.estimatedLoss;
    }

    public BurntStolenIncident estimatedLoss(Double estimatedLoss) {
        this.setEstimatedLoss(estimatedLoss);
        return this;
    }

    public void setEstimatedLoss(Double estimatedLoss) {
        this.estimatedLoss = estimatedLoss;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public BurntStolenIncident contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BurntStolenIncident)) {
            return false;
        }
        return getId() != null && getId().equals(((BurntStolenIncident) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BurntStolenIncident{" +
            "id=" + getId() +
            ", incidentId='" + getIncidentId() + "'" +
            ", incidentDate='" + getIncidentDate() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", estimatedLoss=" + getEstimatedLoss() +
            "}";
    }
}
