package com.insurance.app.web.rest;

import com.insurance.app.domain.BurntStolenIncident;
import com.insurance.app.repository.BurntStolenIncidentRepository;
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
 * REST controller for managing {@link com.insurance.app.domain.BurntStolenIncident}.
 */
@RestController
@RequestMapping("/api/burnt-stolen-incidents")
@Transactional
public class BurntStolenIncidentResource {

    private static final Logger LOG = LoggerFactory.getLogger(BurntStolenIncidentResource.class);

    private static final String ENTITY_NAME = "burntStolenIncident";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BurntStolenIncidentRepository burntStolenIncidentRepository;

    public BurntStolenIncidentResource(BurntStolenIncidentRepository burntStolenIncidentRepository) {
        this.burntStolenIncidentRepository = burntStolenIncidentRepository;
    }

    /**
     * {@code POST  /burnt-stolen-incidents} : Create a new burntStolenIncident.
     *
     * @param burntStolenIncident the burntStolenIncident to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new burntStolenIncident, or with status {@code 400 (Bad Request)} if the burntStolenIncident has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BurntStolenIncident> createBurntStolenIncident(@RequestBody BurntStolenIncident burntStolenIncident)
        throws URISyntaxException {
        LOG.debug("REST request to save BurntStolenIncident : {}", burntStolenIncident);
        if (burntStolenIncident.getId() != null) {
            throw new BadRequestAlertException("A new burntStolenIncident cannot already have an ID", ENTITY_NAME, "idexists");
        }
        burntStolenIncident = burntStolenIncidentRepository.save(burntStolenIncident);
        return ResponseEntity.created(new URI("/api/burnt-stolen-incidents/" + burntStolenIncident.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, burntStolenIncident.getId().toString()))
            .body(burntStolenIncident);
    }

    /**
     * {@code PUT  /burnt-stolen-incidents/:id} : Updates an existing burntStolenIncident.
     *
     * @param id the id of the burntStolenIncident to save.
     * @param burntStolenIncident the burntStolenIncident to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated burntStolenIncident,
     * or with status {@code 400 (Bad Request)} if the burntStolenIncident is not valid,
     * or with status {@code 500 (Internal Server Error)} if the burntStolenIncident couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BurntStolenIncident> updateBurntStolenIncident(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BurntStolenIncident burntStolenIncident
    ) throws URISyntaxException {
        LOG.debug("REST request to update BurntStolenIncident : {}, {}", id, burntStolenIncident);
        if (burntStolenIncident.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, burntStolenIncident.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!burntStolenIncidentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        burntStolenIncident = burntStolenIncidentRepository.save(burntStolenIncident);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, burntStolenIncident.getId().toString()))
            .body(burntStolenIncident);
    }

    /**
     * {@code PATCH  /burnt-stolen-incidents/:id} : Partial updates given fields of an existing burntStolenIncident, field will ignore if it is null
     *
     * @param id the id of the burntStolenIncident to save.
     * @param burntStolenIncident the burntStolenIncident to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated burntStolenIncident,
     * or with status {@code 400 (Bad Request)} if the burntStolenIncident is not valid,
     * or with status {@code 404 (Not Found)} if the burntStolenIncident is not found,
     * or with status {@code 500 (Internal Server Error)} if the burntStolenIncident couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BurntStolenIncident> partialUpdateBurntStolenIncident(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BurntStolenIncident burntStolenIncident
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BurntStolenIncident partially : {}, {}", id, burntStolenIncident);
        if (burntStolenIncident.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, burntStolenIncident.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!burntStolenIncidentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BurntStolenIncident> result = burntStolenIncidentRepository
            .findById(burntStolenIncident.getId())
            .map(existingBurntStolenIncident -> {
                if (burntStolenIncident.getIncidentId() != null) {
                    existingBurntStolenIncident.setIncidentId(burntStolenIncident.getIncidentId());
                }
                if (burntStolenIncident.getIncidentDate() != null) {
                    existingBurntStolenIncident.setIncidentDate(burntStolenIncident.getIncidentDate());
                }
                if (burntStolenIncident.getType() != null) {
                    existingBurntStolenIncident.setType(burntStolenIncident.getType());
                }
                if (burntStolenIncident.getDescription() != null) {
                    existingBurntStolenIncident.setDescription(burntStolenIncident.getDescription());
                }
                if (burntStolenIncident.getEstimatedLoss() != null) {
                    existingBurntStolenIncident.setEstimatedLoss(burntStolenIncident.getEstimatedLoss());
                }

                return existingBurntStolenIncident;
            })
            .map(burntStolenIncidentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, burntStolenIncident.getId().toString())
        );
    }

    /**
     * {@code GET  /burnt-stolen-incidents} : get all the burntStolenIncidents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of burntStolenIncidents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BurntStolenIncident>> getAllBurntStolenIncidents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of BurntStolenIncidents");
        Page<BurntStolenIncident> page = burntStolenIncidentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /burnt-stolen-incidents/:id} : get the "id" burntStolenIncident.
     *
     * @param id the id of the burntStolenIncident to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the burntStolenIncident, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BurntStolenIncident> getBurntStolenIncident(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BurntStolenIncident : {}", id);
        Optional<BurntStolenIncident> burntStolenIncident = burntStolenIncidentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(burntStolenIncident);
    }

    /**
     * {@code DELETE  /burnt-stolen-incidents/:id} : delete the "id" burntStolenIncident.
     *
     * @param id the id of the burntStolenIncident to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBurntStolenIncident(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BurntStolenIncident : {}", id);
        burntStolenIncidentRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
