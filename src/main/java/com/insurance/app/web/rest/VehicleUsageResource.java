package com.insurance.app.web.rest;

import com.insurance.app.domain.VehicleUsage;
import com.insurance.app.repository.VehicleUsageRepository;
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
 * REST controller for managing {@link com.insurance.app.domain.VehicleUsage}.
 */
@RestController
@RequestMapping("/api/vehicle-usages")
@Transactional
public class VehicleUsageResource {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleUsageResource.class);

    private static final String ENTITY_NAME = "vehicleUsage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleUsageRepository vehicleUsageRepository;

    public VehicleUsageResource(VehicleUsageRepository vehicleUsageRepository) {
        this.vehicleUsageRepository = vehicleUsageRepository;
    }

    /**
     * {@code POST  /vehicle-usages} : Create a new vehicleUsage.
     *
     * @param vehicleUsage the vehicleUsage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleUsage, or with status {@code 400 (Bad Request)} if the vehicleUsage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleUsage> createVehicleUsage(@RequestBody VehicleUsage vehicleUsage) throws URISyntaxException {
        LOG.debug("REST request to save VehicleUsage : {}", vehicleUsage);
        if (vehicleUsage.getId() != null) {
            throw new BadRequestAlertException("A new vehicleUsage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleUsage = vehicleUsageRepository.save(vehicleUsage);
        return ResponseEntity.created(new URI("/api/vehicle-usages/" + vehicleUsage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, vehicleUsage.getId().toString()))
            .body(vehicleUsage);
    }

    /**
     * {@code PUT  /vehicle-usages/:id} : Updates an existing vehicleUsage.
     *
     * @param id the id of the vehicleUsage to save.
     * @param vehicleUsage the vehicleUsage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleUsage,
     * or with status {@code 400 (Bad Request)} if the vehicleUsage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleUsage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleUsage> updateVehicleUsage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleUsage vehicleUsage
    ) throws URISyntaxException {
        LOG.debug("REST request to update VehicleUsage : {}, {}", id, vehicleUsage);
        if (vehicleUsage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleUsage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleUsageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleUsage = vehicleUsageRepository.save(vehicleUsage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vehicleUsage.getId().toString()))
            .body(vehicleUsage);
    }

    /**
     * {@code PATCH  /vehicle-usages/:id} : Partial updates given fields of an existing vehicleUsage, field will ignore if it is null
     *
     * @param id the id of the vehicleUsage to save.
     * @param vehicleUsage the vehicleUsage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleUsage,
     * or with status {@code 400 (Bad Request)} if the vehicleUsage is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleUsage is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleUsage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleUsage> partialUpdateVehicleUsage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleUsage vehicleUsage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update VehicleUsage partially : {}, {}", id, vehicleUsage);
        if (vehicleUsage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleUsage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleUsageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleUsage> result = vehicleUsageRepository
            .findById(vehicleUsage.getId())
            .map(existingVehicleUsage -> {
                if (vehicleUsage.getUsageType() != null) {
                    existingVehicleUsage.setUsageType(vehicleUsage.getUsageType());
                }
                if (vehicleUsage.getAnnualMileage() != null) {
                    existingVehicleUsage.setAnnualMileage(vehicleUsage.getAnnualMileage());
                }
                if (vehicleUsage.getCommercialUse() != null) {
                    existingVehicleUsage.setCommercialUse(vehicleUsage.getCommercialUse());
                }

                return existingVehicleUsage;
            })
            .map(vehicleUsageRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vehicleUsage.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-usages} : get all the vehicleUsages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleUsages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleUsage>> getAllVehicleUsages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of VehicleUsages");
        Page<VehicleUsage> page = vehicleUsageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-usages/:id} : get the "id" vehicleUsage.
     *
     * @param id the id of the vehicleUsage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleUsage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleUsage> getVehicleUsage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get VehicleUsage : {}", id);
        Optional<VehicleUsage> vehicleUsage = vehicleUsageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vehicleUsage);
    }

    /**
     * {@code DELETE  /vehicle-usages/:id} : delete the "id" vehicleUsage.
     *
     * @param id the id of the vehicleUsage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleUsage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete VehicleUsage : {}", id);
        vehicleUsageRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
