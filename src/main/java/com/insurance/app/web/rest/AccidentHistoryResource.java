package com.insurance.app.web.rest;

import com.insurance.app.domain.AccidentHistory;
import com.insurance.app.repository.AccidentHistoryRepository;
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
 * REST controller for managing {@link com.insurance.app.domain.AccidentHistory}.
 */
@RestController
@RequestMapping("/api/accident-histories")
@Transactional
public class AccidentHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccidentHistoryResource.class);

    private static final String ENTITY_NAME = "accidentHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccidentHistoryRepository accidentHistoryRepository;

    public AccidentHistoryResource(AccidentHistoryRepository accidentHistoryRepository) {
        this.accidentHistoryRepository = accidentHistoryRepository;
    }

    /**
     * {@code POST  /accident-histories} : Create a new accidentHistory.
     *
     * @param accidentHistory the accidentHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accidentHistory, or with status {@code 400 (Bad Request)} if the accidentHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccidentHistory> createAccidentHistory(@RequestBody AccidentHistory accidentHistory) throws URISyntaxException {
        LOG.debug("REST request to save AccidentHistory : {}", accidentHistory);
        if (accidentHistory.getId() != null) {
            throw new BadRequestAlertException("A new accidentHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accidentHistory = accidentHistoryRepository.save(accidentHistory);
        return ResponseEntity.created(new URI("/api/accident-histories/" + accidentHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, accidentHistory.getId().toString()))
            .body(accidentHistory);
    }

    /**
     * {@code PUT  /accident-histories/:id} : Updates an existing accidentHistory.
     *
     * @param id the id of the accidentHistory to save.
     * @param accidentHistory the accidentHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accidentHistory,
     * or with status {@code 400 (Bad Request)} if the accidentHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accidentHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccidentHistory> updateAccidentHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccidentHistory accidentHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update AccidentHistory : {}, {}", id, accidentHistory);
        if (accidentHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accidentHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accidentHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accidentHistory = accidentHistoryRepository.save(accidentHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accidentHistory.getId().toString()))
            .body(accidentHistory);
    }

    /**
     * {@code PATCH  /accident-histories/:id} : Partial updates given fields of an existing accidentHistory, field will ignore if it is null
     *
     * @param id the id of the accidentHistory to save.
     * @param accidentHistory the accidentHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accidentHistory,
     * or with status {@code 400 (Bad Request)} if the accidentHistory is not valid,
     * or with status {@code 404 (Not Found)} if the accidentHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the accidentHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccidentHistory> partialUpdateAccidentHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccidentHistory accidentHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AccidentHistory partially : {}, {}", id, accidentHistory);
        if (accidentHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accidentHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accidentHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccidentHistory> result = accidentHistoryRepository
            .findById(accidentHistory.getId())
            .map(existingAccidentHistory -> {
                if (accidentHistory.getAccidentId() != null) {
                    existingAccidentHistory.setAccidentId(accidentHistory.getAccidentId());
                }
                if (accidentHistory.getAccidentDate() != null) {
                    existingAccidentHistory.setAccidentDate(accidentHistory.getAccidentDate());
                }
                if (accidentHistory.getSeverity() != null) {
                    existingAccidentHistory.setSeverity(accidentHistory.getSeverity());
                }
                if (accidentHistory.getDescription() != null) {
                    existingAccidentHistory.setDescription(accidentHistory.getDescription());
                }
                if (accidentHistory.getRepairCost() != null) {
                    existingAccidentHistory.setRepairCost(accidentHistory.getRepairCost());
                }

                return existingAccidentHistory;
            })
            .map(accidentHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accidentHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /accident-histories} : get all the accidentHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accidentHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccidentHistory>> getAllAccidentHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AccidentHistories");
        Page<AccidentHistory> page = accidentHistoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /accident-histories/:id} : get the "id" accidentHistory.
     *
     * @param id the id of the accidentHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accidentHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccidentHistory> getAccidentHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AccidentHistory : {}", id);
        Optional<AccidentHistory> accidentHistory = accidentHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accidentHistory);
    }

    /**
     * {@code DELETE  /accident-histories/:id} : delete the "id" accidentHistory.
     *
     * @param id the id of the accidentHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccidentHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AccidentHistory : {}", id);
        accidentHistoryRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
