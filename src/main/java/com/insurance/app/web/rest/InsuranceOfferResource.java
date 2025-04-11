package com.insurance.app.web.rest;

import com.insurance.app.domain.InsuranceOffer;
import com.insurance.app.repository.InsuranceOfferRepository;
import com.insurance.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.insurance.app.domain.InsuranceOffer}.
 */
@RestController
@RequestMapping("/api/insurance-offers")
@Transactional
public class InsuranceOfferResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceOfferResource.class);

    private static final String ENTITY_NAME = "insuranceOffer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceOfferRepository insuranceOfferRepository;

    public InsuranceOfferResource(InsuranceOfferRepository insuranceOfferRepository) {
        this.insuranceOfferRepository = insuranceOfferRepository;
    }

    /**
     * {@code POST  /insurance-offers} : Create a new insuranceOffer.
     *
     * @param insuranceOffer the insuranceOffer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceOffer, or with status {@code 400 (Bad Request)} if the insuranceOffer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsuranceOffer> createInsuranceOffer(@RequestBody InsuranceOffer insuranceOffer) throws URISyntaxException {
        LOG.debug("REST request to save InsuranceOffer : {}", insuranceOffer);
        if (insuranceOffer.getId() != null) {
            throw new BadRequestAlertException("A new insuranceOffer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insuranceOffer = insuranceOfferRepository.save(insuranceOffer);
        return ResponseEntity.created(new URI("/api/insurance-offers/" + insuranceOffer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, insuranceOffer.getId().toString()))
            .body(insuranceOffer);
    }

    /**
     * {@code PUT  /insurance-offers/:id} : Updates an existing insuranceOffer.
     *
     * @param id the id of the insuranceOffer to save.
     * @param insuranceOffer the insuranceOffer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceOffer,
     * or with status {@code 400 (Bad Request)} if the insuranceOffer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceOffer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsuranceOffer> updateInsuranceOffer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceOffer insuranceOffer
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsuranceOffer : {}, {}", id, insuranceOffer);
        if (insuranceOffer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceOffer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceOfferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insuranceOffer = insuranceOfferRepository.save(insuranceOffer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceOffer.getId().toString()))
            .body(insuranceOffer);
    }

    /**
     * {@code PATCH  /insurance-offers/:id} : Partial updates given fields of an existing insuranceOffer, field will ignore if it is null
     *
     * @param id the id of the insuranceOffer to save.
     * @param insuranceOffer the insuranceOffer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceOffer,
     * or with status {@code 400 (Bad Request)} if the insuranceOffer is not valid,
     * or with status {@code 404 (Not Found)} if the insuranceOffer is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuranceOffer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsuranceOffer> partialUpdateInsuranceOffer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceOffer insuranceOffer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InsuranceOffer partially : {}, {}", id, insuranceOffer);
        if (insuranceOffer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceOffer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceOfferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuranceOffer> result = insuranceOfferRepository
            .findById(insuranceOffer.getId())
            .map(existingInsuranceOffer -> {
                if (insuranceOffer.getOfferId() != null) {
                    existingInsuranceOffer.setOfferId(insuranceOffer.getOfferId());
                }
                if (insuranceOffer.getOfferName() != null) {
                    existingInsuranceOffer.setOfferName(insuranceOffer.getOfferName());
                }
                if (insuranceOffer.getPrice() != null) {
                    existingInsuranceOffer.setPrice(insuranceOffer.getPrice());
                }
                if (insuranceOffer.getCoverageDetails() != null) {
                    existingInsuranceOffer.setCoverageDetails(insuranceOffer.getCoverageDetails());
                }
                if (insuranceOffer.getTermsAndConditions() != null) {
                    existingInsuranceOffer.setTermsAndConditions(insuranceOffer.getTermsAndConditions());
                }

                return existingInsuranceOffer;
            })
            .map(insuranceOfferRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceOffer.getId().toString())
        );
    }

    /**
     * {@code GET  /insurance-offers} : get all the insuranceOffers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceOffers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsuranceOffer>> getAllInsuranceOffers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of InsuranceOffers");
        Page<InsuranceOffer> page = insuranceOfferRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-offers/:id} : get the "id" insuranceOffer.
     *
     * @param id the id of the insuranceOffer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceOffer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceOffer> getInsuranceOffer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsuranceOffer : {}", id);
        Optional<InsuranceOffer> insuranceOffer = insuranceOfferRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(insuranceOffer);
    }

    /**
     * {@code DELETE  /insurance-offers/:id} : delete the "id" insuranceOffer.
     *
     * @param id the id of the insuranceOffer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuranceOffer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsuranceOffer : {}", id);
        insuranceOfferRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
