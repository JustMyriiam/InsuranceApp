package com.insurance.app.web.rest;

import com.insurance.app.domain.TrafficViolation;
import com.insurance.app.repository.TrafficViolationRepository;
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
 * REST controller for managing {@link com.insurance.app.domain.TrafficViolation}.
 */
@RestController
@RequestMapping("/api/traffic-violations")
@Transactional
public class TrafficViolationResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrafficViolationResource.class);

    private static final String ENTITY_NAME = "trafficViolation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrafficViolationRepository trafficViolationRepository;

    public TrafficViolationResource(TrafficViolationRepository trafficViolationRepository) {
        this.trafficViolationRepository = trafficViolationRepository;
    }

    /**
     * {@code POST  /traffic-violations} : Create a new trafficViolation.
     *
     * @param trafficViolation the trafficViolation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trafficViolation, or with status {@code 400 (Bad Request)} if the trafficViolation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrafficViolation> createTrafficViolation(@RequestBody TrafficViolation trafficViolation)
        throws URISyntaxException {
        LOG.debug("REST request to save TrafficViolation : {}", trafficViolation);
        if (trafficViolation.getId() != null) {
            throw new BadRequestAlertException("A new trafficViolation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trafficViolation = trafficViolationRepository.save(trafficViolation);
        return ResponseEntity.created(new URI("/api/traffic-violations/" + trafficViolation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trafficViolation.getId().toString()))
            .body(trafficViolation);
    }

    /**
     * {@code PUT  /traffic-violations/:id} : Updates an existing trafficViolation.
     *
     * @param id the id of the trafficViolation to save.
     * @param trafficViolation the trafficViolation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trafficViolation,
     * or with status {@code 400 (Bad Request)} if the trafficViolation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trafficViolation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrafficViolation> updateTrafficViolation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrafficViolation trafficViolation
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrafficViolation : {}, {}", id, trafficViolation);
        if (trafficViolation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trafficViolation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trafficViolationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trafficViolation = trafficViolationRepository.save(trafficViolation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trafficViolation.getId().toString()))
            .body(trafficViolation);
    }

    /**
     * {@code PATCH  /traffic-violations/:id} : Partial updates given fields of an existing trafficViolation, field will ignore if it is null
     *
     * @param id the id of the trafficViolation to save.
     * @param trafficViolation the trafficViolation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trafficViolation,
     * or with status {@code 400 (Bad Request)} if the trafficViolation is not valid,
     * or with status {@code 404 (Not Found)} if the trafficViolation is not found,
     * or with status {@code 500 (Internal Server Error)} if the trafficViolation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrafficViolation> partialUpdateTrafficViolation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrafficViolation trafficViolation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrafficViolation partially : {}, {}", id, trafficViolation);
        if (trafficViolation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trafficViolation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trafficViolationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrafficViolation> result = trafficViolationRepository
            .findById(trafficViolation.getId())
            .map(existingTrafficViolation -> {
                if (trafficViolation.getViolationType() != null) {
                    existingTrafficViolation.setViolationType(trafficViolation.getViolationType());
                }
                if (trafficViolation.getViolationDate() != null) {
                    existingTrafficViolation.setViolationDate(trafficViolation.getViolationDate());
                }
                if (trafficViolation.getPenaltyPoints() != null) {
                    existingTrafficViolation.setPenaltyPoints(trafficViolation.getPenaltyPoints());
                }

                return existingTrafficViolation;
            })
            .map(trafficViolationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trafficViolation.getId().toString())
        );
    }

    /**
     * {@code GET  /traffic-violations} : get all the trafficViolations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trafficViolations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrafficViolation>> getAllTrafficViolations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of TrafficViolations");
        Page<TrafficViolation> page = trafficViolationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /traffic-violations/:id} : get the "id" trafficViolation.
     *
     * @param id the id of the trafficViolation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trafficViolation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrafficViolation> getTrafficViolation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TrafficViolation : {}", id);
        Optional<TrafficViolation> trafficViolation = trafficViolationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trafficViolation);
    }

    /**
     * {@code DELETE  /traffic-violations/:id} : delete the "id" trafficViolation.
     *
     * @param id the id of the trafficViolation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrafficViolation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TrafficViolation : {}", id);
        trafficViolationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
