package com.insurance.app.web.rest;

import com.insurance.app.domain.VehicleAccessory;
import com.insurance.app.repository.VehicleAccessoryRepository;
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
 * REST controller for managing {@link com.insurance.app.domain.VehicleAccessory}.
 */
@RestController
@RequestMapping("/api/vehicle-accessories")
@Transactional
public class VehicleAccessoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleAccessoryResource.class);

    private static final String ENTITY_NAME = "vehicleAccessory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleAccessoryRepository vehicleAccessoryRepository;

    public VehicleAccessoryResource(VehicleAccessoryRepository vehicleAccessoryRepository) {
        this.vehicleAccessoryRepository = vehicleAccessoryRepository;
    }

    /**
     * {@code POST  /vehicle-accessories} : Create a new vehicleAccessory.
     *
     * @param vehicleAccessory the vehicleAccessory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleAccessory, or with status {@code 400 (Bad Request)} if the vehicleAccessory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleAccessory> createVehicleAccessory(@RequestBody VehicleAccessory vehicleAccessory)
        throws URISyntaxException {
        LOG.debug("REST request to save VehicleAccessory : {}", vehicleAccessory);
        if (vehicleAccessory.getId() != null) {
            throw new BadRequestAlertException("A new vehicleAccessory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleAccessory = vehicleAccessoryRepository.save(vehicleAccessory);
        return ResponseEntity.created(new URI("/api/vehicle-accessories/" + vehicleAccessory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, vehicleAccessory.getId().toString()))
            .body(vehicleAccessory);
    }

    /**
     * {@code PUT  /vehicle-accessories/:id} : Updates an existing vehicleAccessory.
     *
     * @param id the id of the vehicleAccessory to save.
     * @param vehicleAccessory the vehicleAccessory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleAccessory,
     * or with status {@code 400 (Bad Request)} if the vehicleAccessory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleAccessory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleAccessory> updateVehicleAccessory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleAccessory vehicleAccessory
    ) throws URISyntaxException {
        LOG.debug("REST request to update VehicleAccessory : {}, {}", id, vehicleAccessory);
        if (vehicleAccessory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleAccessory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleAccessoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleAccessory = vehicleAccessoryRepository.save(vehicleAccessory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vehicleAccessory.getId().toString()))
            .body(vehicleAccessory);
    }

    /**
     * {@code PATCH  /vehicle-accessories/:id} : Partial updates given fields of an existing vehicleAccessory, field will ignore if it is null
     *
     * @param id the id of the vehicleAccessory to save.
     * @param vehicleAccessory the vehicleAccessory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleAccessory,
     * or with status {@code 400 (Bad Request)} if the vehicleAccessory is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleAccessory is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleAccessory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleAccessory> partialUpdateVehicleAccessory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleAccessory vehicleAccessory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update VehicleAccessory partially : {}, {}", id, vehicleAccessory);
        if (vehicleAccessory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleAccessory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleAccessoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleAccessory> result = vehicleAccessoryRepository
            .findById(vehicleAccessory.getId())
            .map(existingVehicleAccessory -> {
                if (vehicleAccessory.getAccessoryId() != null) {
                    existingVehicleAccessory.setAccessoryId(vehicleAccessory.getAccessoryId());
                }
                if (vehicleAccessory.getName() != null) {
                    existingVehicleAccessory.setName(vehicleAccessory.getName());
                }
                if (vehicleAccessory.getType() != null) {
                    existingVehicleAccessory.setType(vehicleAccessory.getType());
                }
                if (vehicleAccessory.getFactoryInstalled() != null) {
                    existingVehicleAccessory.setFactoryInstalled(vehicleAccessory.getFactoryInstalled());
                }

                return existingVehicleAccessory;
            })
            .map(vehicleAccessoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vehicleAccessory.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-accessories} : get all the vehicleAccessories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleAccessories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleAccessory>> getAllVehicleAccessories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of VehicleAccessories");
        Page<VehicleAccessory> page = vehicleAccessoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-accessories/:id} : get the "id" vehicleAccessory.
     *
     * @param id the id of the vehicleAccessory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleAccessory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleAccessory> getVehicleAccessory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get VehicleAccessory : {}", id);
        Optional<VehicleAccessory> vehicleAccessory = vehicleAccessoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vehicleAccessory);
    }

    /**
     * {@code DELETE  /vehicle-accessories/:id} : delete the "id" vehicleAccessory.
     *
     * @param id the id of the vehicleAccessory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleAccessory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete VehicleAccessory : {}", id);
        vehicleAccessoryRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
