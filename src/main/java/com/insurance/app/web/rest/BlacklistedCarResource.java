package com.insurance.app.web.rest;

import com.insurance.app.domain.BlacklistedCar;
import com.insurance.app.repository.BlacklistedCarRepository;
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
 * REST controller for managing {@link com.insurance.app.domain.BlacklistedCar}.
 */
@RestController
@RequestMapping("/api/blacklisted-cars")
@Transactional
public class BlacklistedCarResource {

    private static final Logger LOG = LoggerFactory.getLogger(BlacklistedCarResource.class);

    private static final String ENTITY_NAME = "blacklistedCar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlacklistedCarRepository blacklistedCarRepository;

    public BlacklistedCarResource(BlacklistedCarRepository blacklistedCarRepository) {
        this.blacklistedCarRepository = blacklistedCarRepository;
    }

    /**
     * {@code POST  /blacklisted-cars} : Create a new blacklistedCar.
     *
     * @param blacklistedCar the blacklistedCar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blacklistedCar, or with status {@code 400 (Bad Request)} if the blacklistedCar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BlacklistedCar> createBlacklistedCar(@RequestBody BlacklistedCar blacklistedCar) throws URISyntaxException {
        LOG.debug("REST request to save BlacklistedCar : {}", blacklistedCar);
        if (blacklistedCar.getId() != null) {
            throw new BadRequestAlertException("A new blacklistedCar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        blacklistedCar = blacklistedCarRepository.save(blacklistedCar);
        return ResponseEntity.created(new URI("/api/blacklisted-cars/" + blacklistedCar.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, blacklistedCar.getId().toString()))
            .body(blacklistedCar);
    }

    /**
     * {@code PUT  /blacklisted-cars/:id} : Updates an existing blacklistedCar.
     *
     * @param id the id of the blacklistedCar to save.
     * @param blacklistedCar the blacklistedCar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blacklistedCar,
     * or with status {@code 400 (Bad Request)} if the blacklistedCar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blacklistedCar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BlacklistedCar> updateBlacklistedCar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BlacklistedCar blacklistedCar
    ) throws URISyntaxException {
        LOG.debug("REST request to update BlacklistedCar : {}, {}", id, blacklistedCar);
        if (blacklistedCar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blacklistedCar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blacklistedCarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        blacklistedCar = blacklistedCarRepository.save(blacklistedCar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, blacklistedCar.getId().toString()))
            .body(blacklistedCar);
    }

    /**
     * {@code PATCH  /blacklisted-cars/:id} : Partial updates given fields of an existing blacklistedCar, field will ignore if it is null
     *
     * @param id the id of the blacklistedCar to save.
     * @param blacklistedCar the blacklistedCar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blacklistedCar,
     * or with status {@code 400 (Bad Request)} if the blacklistedCar is not valid,
     * or with status {@code 404 (Not Found)} if the blacklistedCar is not found,
     * or with status {@code 500 (Internal Server Error)} if the blacklistedCar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BlacklistedCar> partialUpdateBlacklistedCar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BlacklistedCar blacklistedCar
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BlacklistedCar partially : {}, {}", id, blacklistedCar);
        if (blacklistedCar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blacklistedCar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blacklistedCarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BlacklistedCar> result = blacklistedCarRepository
            .findById(blacklistedCar.getId())
            .map(existingBlacklistedCar -> {
                if (blacklistedCar.getReason() != null) {
                    existingBlacklistedCar.setReason(blacklistedCar.getReason());
                }
                if (blacklistedCar.getBlacklistDate() != null) {
                    existingBlacklistedCar.setBlacklistDate(blacklistedCar.getBlacklistDate());
                }

                return existingBlacklistedCar;
            })
            .map(blacklistedCarRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, blacklistedCar.getId().toString())
        );
    }

    /**
     * {@code GET  /blacklisted-cars} : get all the blacklistedCars.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blacklistedCars in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BlacklistedCar>> getAllBlacklistedCars(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of BlacklistedCars");
        Page<BlacklistedCar> page = blacklistedCarRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /blacklisted-cars/:id} : get the "id" blacklistedCar.
     *
     * @param id the id of the blacklistedCar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blacklistedCar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BlacklistedCar> getBlacklistedCar(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BlacklistedCar : {}", id);
        Optional<BlacklistedCar> blacklistedCar = blacklistedCarRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(blacklistedCar);
    }

    /**
     * {@code DELETE  /blacklisted-cars/:id} : delete the "id" blacklistedCar.
     *
     * @param id the id of the blacklistedCar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlacklistedCar(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BlacklistedCar : {}", id);
        blacklistedCarRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
