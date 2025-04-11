package com.insurance.app.web.rest;

import com.insurance.app.domain.DocumentSinister;
import com.insurance.app.repository.DocumentSinisterRepository;
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
 * REST controller for managing {@link com.insurance.app.domain.DocumentSinister}.
 */
@RestController
@RequestMapping("/api/document-sinisters")
@Transactional
public class DocumentSinisterResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentSinisterResource.class);

    private static final String ENTITY_NAME = "documentSinister";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentSinisterRepository documentSinisterRepository;

    public DocumentSinisterResource(DocumentSinisterRepository documentSinisterRepository) {
        this.documentSinisterRepository = documentSinisterRepository;
    }

    /**
     * {@code POST  /document-sinisters} : Create a new documentSinister.
     *
     * @param documentSinister the documentSinister to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentSinister, or with status {@code 400 (Bad Request)} if the documentSinister has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentSinister> createDocumentSinister(@RequestBody DocumentSinister documentSinister)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentSinister : {}", documentSinister);
        if (documentSinister.getId() != null) {
            throw new BadRequestAlertException("A new documentSinister cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentSinister = documentSinisterRepository.save(documentSinister);
        return ResponseEntity.created(new URI("/api/document-sinisters/" + documentSinister.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, documentSinister.getId().toString()))
            .body(documentSinister);
    }

    /**
     * {@code PUT  /document-sinisters/:id} : Updates an existing documentSinister.
     *
     * @param id the id of the documentSinister to save.
     * @param documentSinister the documentSinister to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentSinister,
     * or with status {@code 400 (Bad Request)} if the documentSinister is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentSinister couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentSinister> updateDocumentSinister(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentSinister documentSinister
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentSinister : {}, {}", id, documentSinister);
        if (documentSinister.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentSinister.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentSinisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentSinister = documentSinisterRepository.save(documentSinister);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentSinister.getId().toString()))
            .body(documentSinister);
    }

    /**
     * {@code PATCH  /document-sinisters/:id} : Partial updates given fields of an existing documentSinister, field will ignore if it is null
     *
     * @param id the id of the documentSinister to save.
     * @param documentSinister the documentSinister to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentSinister,
     * or with status {@code 400 (Bad Request)} if the documentSinister is not valid,
     * or with status {@code 404 (Not Found)} if the documentSinister is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentSinister couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentSinister> partialUpdateDocumentSinister(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentSinister documentSinister
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentSinister partially : {}, {}", id, documentSinister);
        if (documentSinister.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentSinister.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentSinisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentSinister> result = documentSinisterRepository
            .findById(documentSinister.getId())
            .map(existingDocumentSinister -> {
                if (documentSinister.getDocumentId() != null) {
                    existingDocumentSinister.setDocumentId(documentSinister.getDocumentId());
                }
                if (documentSinister.getDocumentName() != null) {
                    existingDocumentSinister.setDocumentName(documentSinister.getDocumentName());
                }
                if (documentSinister.getIssueDate() != null) {
                    existingDocumentSinister.setIssueDate(documentSinister.getIssueDate());
                }
                if (documentSinister.getExpiryDate() != null) {
                    existingDocumentSinister.setExpiryDate(documentSinister.getExpiryDate());
                }
                if (documentSinister.getAssociatedSinister() != null) {
                    existingDocumentSinister.setAssociatedSinister(documentSinister.getAssociatedSinister());
                }

                return existingDocumentSinister;
            })
            .map(documentSinisterRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentSinister.getId().toString())
        );
    }

    /**
     * {@code GET  /document-sinisters} : get all the documentSinisters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentSinisters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentSinister>> getAllDocumentSinisters(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of DocumentSinisters");
        Page<DocumentSinister> page = documentSinisterRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-sinisters/:id} : get the "id" documentSinister.
     *
     * @param id the id of the documentSinister to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentSinister, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentSinister> getDocumentSinister(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentSinister : {}", id);
        Optional<DocumentSinister> documentSinister = documentSinisterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(documentSinister);
    }

    /**
     * {@code DELETE  /document-sinisters/:id} : delete the "id" documentSinister.
     *
     * @param id the id of the documentSinister to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentSinister(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentSinister : {}", id);
        documentSinisterRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
