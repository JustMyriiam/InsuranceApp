package com.insurance.app.web.rest;

import com.insurance.app.domain.LocationRisk;
import com.insurance.app.repository.LocationRiskRepository;
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
 * REST controller for managing {@link com.insurance.app.domain.LocationRisk}.
 */
@RestController
@RequestMapping("/api/location-risks")
@Transactional
public class LocationRiskResource {

    private static final Logger LOG = LoggerFactory.getLogger(LocationRiskResource.class);

    private static final String ENTITY_NAME = "locationRisk";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationRiskRepository locationRiskRepository;

    public LocationRiskResource(LocationRiskRepository locationRiskRepository) {
        this.locationRiskRepository = locationRiskRepository;
    }

    /**
     * {@code POST  /location-risks} : Create a new locationRisk.
     *
     * @param locationRisk the locationRisk to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationRisk, or with status {@code 400 (Bad Request)} if the locationRisk has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LocationRisk> createLocationRisk(@RequestBody LocationRisk locationRisk) throws URISyntaxException {
        LOG.debug("REST request to save LocationRisk : {}", locationRisk);
        if (locationRisk.getId() != null) {
            throw new BadRequestAlertException("A new locationRisk cannot already have an ID", ENTITY_NAME, "idexists");
        }
        locationRisk = locationRiskRepository.save(locationRisk);
        return ResponseEntity.created(new URI("/api/location-risks/" + locationRisk.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, locationRisk.getId().toString()))
            .body(locationRisk);
    }

    /**
     * {@code PUT  /location-risks/:id} : Updates an existing locationRisk.
     *
     * @param id the id of the locationRisk to save.
     * @param locationRisk the locationRisk to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationRisk,
     * or with status {@code 400 (Bad Request)} if the locationRisk is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationRisk couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LocationRisk> updateLocationRisk(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationRisk locationRisk
    ) throws URISyntaxException {
        LOG.debug("REST request to update LocationRisk : {}, {}", id, locationRisk);
        if (locationRisk.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationRisk.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationRiskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        locationRisk = locationRiskRepository.save(locationRisk);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, locationRisk.getId().toString()))
            .body(locationRisk);
    }

    /**
     * {@code PATCH  /location-risks/:id} : Partial updates given fields of an existing locationRisk, field will ignore if it is null
     *
     * @param id the id of the locationRisk to save.
     * @param locationRisk the locationRisk to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationRisk,
     * or with status {@code 400 (Bad Request)} if the locationRisk is not valid,
     * or with status {@code 404 (Not Found)} if the locationRisk is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationRisk couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocationRisk> partialUpdateLocationRisk(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationRisk locationRisk
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LocationRisk partially : {}, {}", id, locationRisk);
        if (locationRisk.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationRisk.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationRiskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationRisk> result = locationRiskRepository
            .findById(locationRisk.getId())
            .map(existingLocationRisk -> {
                if (locationRisk.getRegion() != null) {
                    existingLocationRisk.setRegion(locationRisk.getRegion());
                }
                if (locationRisk.getTheftRisk() != null) {
                    existingLocationRisk.setTheftRisk(locationRisk.getTheftRisk());
                }
                if (locationRisk.getAccidentRisk() != null) {
                    existingLocationRisk.setAccidentRisk(locationRisk.getAccidentRisk());
                }
                if (locationRisk.getWeatherRisk() != null) {
                    existingLocationRisk.setWeatherRisk(locationRisk.getWeatherRisk());
                }

                return existingLocationRisk;
            })
            .map(locationRiskRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, locationRisk.getId().toString())
        );
    }

    /**
     * {@code GET  /location-risks} : get all the locationRisks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationRisks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LocationRisk>> getAllLocationRisks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of LocationRisks");
        Page<LocationRisk> page = locationRiskRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /location-risks/:id} : get the "id" locationRisk.
     *
     * @param id the id of the locationRisk to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationRisk, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LocationRisk> getLocationRisk(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LocationRisk : {}", id);
        Optional<LocationRisk> locationRisk = locationRiskRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(locationRisk);
    }

    /**
     * {@code DELETE  /location-risks/:id} : delete the "id" locationRisk.
     *
     * @param id the id of the locationRisk to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocationRisk(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LocationRisk : {}", id);
        locationRiskRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
