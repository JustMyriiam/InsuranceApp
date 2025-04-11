package com.insurance.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentSinister.
 */
@Entity
@Table(name = "document_sinister")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentSinister implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "issue_date")
    private Instant issueDate;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Column(name = "associated_sinister")
    private String associatedSinister;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "documentSinister")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract", "documentSinister" }, allowSetters = true)
    private Set<AccidentHistory> accidentHistories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentSinister id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public DocumentSinister documentId(String documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public DocumentSinister documentName(String documentName) {
        this.setDocumentName(documentName);
        return this;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Instant getIssueDate() {
        return this.issueDate;
    }

    public DocumentSinister issueDate(Instant issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
    }

    public Instant getExpiryDate() {
        return this.expiryDate;
    }

    public DocumentSinister expiryDate(Instant expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAssociatedSinister() {
        return this.associatedSinister;
    }

    public DocumentSinister associatedSinister(String associatedSinister) {
        this.setAssociatedSinister(associatedSinister);
        return this;
    }

    public void setAssociatedSinister(String associatedSinister) {
        this.associatedSinister = associatedSinister;
    }

    public Set<AccidentHistory> getAccidentHistories() {
        return this.accidentHistories;
    }

    public void setAccidentHistories(Set<AccidentHistory> accidentHistories) {
        if (this.accidentHistories != null) {
            this.accidentHistories.forEach(i -> i.setDocumentSinister(null));
        }
        if (accidentHistories != null) {
            accidentHistories.forEach(i -> i.setDocumentSinister(this));
        }
        this.accidentHistories = accidentHistories;
    }

    public DocumentSinister accidentHistories(Set<AccidentHistory> accidentHistories) {
        this.setAccidentHistories(accidentHistories);
        return this;
    }

    public DocumentSinister addAccidentHistory(AccidentHistory accidentHistory) {
        this.accidentHistories.add(accidentHistory);
        accidentHistory.setDocumentSinister(this);
        return this;
    }

    public DocumentSinister removeAccidentHistory(AccidentHistory accidentHistory) {
        this.accidentHistories.remove(accidentHistory);
        accidentHistory.setDocumentSinister(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentSinister)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentSinister) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentSinister{" +
            "id=" + getId() +
            ", documentId='" + getDocumentId() + "'" +
            ", documentName='" + getDocumentName() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", associatedSinister='" + getAssociatedSinister() + "'" +
            "}";
    }
}
