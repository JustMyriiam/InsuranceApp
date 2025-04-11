package com.insurance.app.web.rest;

import static com.insurance.app.domain.DocumentSinisterAsserts.*;
import static com.insurance.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.app.IntegrationTest;
import com.insurance.app.domain.DocumentSinister;
import com.insurance.app.repository.DocumentSinisterRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DocumentSinisterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentSinisterResourceIT {

    private static final String DEFAULT_DOCUMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ASSOCIATED_SINISTER = "AAAAAAAAAA";
    private static final String UPDATED_ASSOCIATED_SINISTER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-sinisters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentSinisterRepository documentSinisterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentSinisterMockMvc;

    private DocumentSinister documentSinister;

    private DocumentSinister insertedDocumentSinister;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentSinister createEntity() {
        return new DocumentSinister()
            .documentId(DEFAULT_DOCUMENT_ID)
            .documentName(DEFAULT_DOCUMENT_NAME)
            .issueDate(DEFAULT_ISSUE_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .associatedSinister(DEFAULT_ASSOCIATED_SINISTER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentSinister createUpdatedEntity() {
        return new DocumentSinister()
            .documentId(UPDATED_DOCUMENT_ID)
            .documentName(UPDATED_DOCUMENT_NAME)
            .issueDate(UPDATED_ISSUE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .associatedSinister(UPDATED_ASSOCIATED_SINISTER);
    }

    @BeforeEach
    public void initTest() {
        documentSinister = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDocumentSinister != null) {
            documentSinisterRepository.delete(insertedDocumentSinister);
            insertedDocumentSinister = null;
        }
    }

    @Test
    @Transactional
    void createDocumentSinister() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentSinister
        var returnedDocumentSinister = om.readValue(
            restDocumentSinisterMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(documentSinister))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentSinister.class
        );

        // Validate the DocumentSinister in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDocumentSinisterUpdatableFieldsEquals(returnedDocumentSinister, getPersistedDocumentSinister(returnedDocumentSinister));

        insertedDocumentSinister = returnedDocumentSinister;
    }

    @Test
    @Transactional
    void createDocumentSinisterWithExistingId() throws Exception {
        // Create the DocumentSinister with an existing ID
        documentSinister.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentSinisterMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentSinister))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSinister in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentSinisters() throws Exception {
        // Initialize the database
        insertedDocumentSinister = documentSinisterRepository.saveAndFlush(documentSinister);

        // Get all the documentSinisterList
        restDocumentSinisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentSinister.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID)))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].associatedSinister").value(hasItem(DEFAULT_ASSOCIATED_SINISTER)));
    }

    @Test
    @Transactional
    void getDocumentSinister() throws Exception {
        // Initialize the database
        insertedDocumentSinister = documentSinisterRepository.saveAndFlush(documentSinister);

        // Get the documentSinister
        restDocumentSinisterMockMvc
            .perform(get(ENTITY_API_URL_ID, documentSinister.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentSinister.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID))
            .andExpect(jsonPath("$.documentName").value(DEFAULT_DOCUMENT_NAME))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.associatedSinister").value(DEFAULT_ASSOCIATED_SINISTER));
    }

    @Test
    @Transactional
    void getNonExistingDocumentSinister() throws Exception {
        // Get the documentSinister
        restDocumentSinisterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentSinister() throws Exception {
        // Initialize the database
        insertedDocumentSinister = documentSinisterRepository.saveAndFlush(documentSinister);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentSinister
        DocumentSinister updatedDocumentSinister = documentSinisterRepository.findById(documentSinister.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentSinister are not directly saved in db
        em.detach(updatedDocumentSinister);
        updatedDocumentSinister
            .documentId(UPDATED_DOCUMENT_ID)
            .documentName(UPDATED_DOCUMENT_NAME)
            .issueDate(UPDATED_ISSUE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .associatedSinister(UPDATED_ASSOCIATED_SINISTER);

        restDocumentSinisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentSinister.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDocumentSinister))
            )
            .andExpect(status().isOk());

        // Validate the DocumentSinister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentSinisterToMatchAllProperties(updatedDocumentSinister);
    }

    @Test
    @Transactional
    void putNonExistingDocumentSinister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSinister.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentSinisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentSinister.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentSinister))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSinister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentSinister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSinister.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSinisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentSinister))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSinister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentSinister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSinister.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSinisterMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentSinister))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentSinister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentSinisterWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentSinister = documentSinisterRepository.saveAndFlush(documentSinister);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentSinister using partial update
        DocumentSinister partialUpdatedDocumentSinister = new DocumentSinister();
        partialUpdatedDocumentSinister.setId(documentSinister.getId());

        partialUpdatedDocumentSinister
            .documentId(UPDATED_DOCUMENT_ID)
            .issueDate(UPDATED_ISSUE_DATE)
            .associatedSinister(UPDATED_ASSOCIATED_SINISTER);

        restDocumentSinisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentSinister.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentSinister))
            )
            .andExpect(status().isOk());

        // Validate the DocumentSinister in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentSinisterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentSinister, documentSinister),
            getPersistedDocumentSinister(documentSinister)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentSinisterWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentSinister = documentSinisterRepository.saveAndFlush(documentSinister);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentSinister using partial update
        DocumentSinister partialUpdatedDocumentSinister = new DocumentSinister();
        partialUpdatedDocumentSinister.setId(documentSinister.getId());

        partialUpdatedDocumentSinister
            .documentId(UPDATED_DOCUMENT_ID)
            .documentName(UPDATED_DOCUMENT_NAME)
            .issueDate(UPDATED_ISSUE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .associatedSinister(UPDATED_ASSOCIATED_SINISTER);

        restDocumentSinisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentSinister.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentSinister))
            )
            .andExpect(status().isOk());

        // Validate the DocumentSinister in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentSinisterUpdatableFieldsEquals(
            partialUpdatedDocumentSinister,
            getPersistedDocumentSinister(partialUpdatedDocumentSinister)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentSinister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSinister.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentSinisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentSinister.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentSinister))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSinister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentSinister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSinister.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSinisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentSinister))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSinister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentSinister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSinister.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSinisterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentSinister))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentSinister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentSinister() throws Exception {
        // Initialize the database
        insertedDocumentSinister = documentSinisterRepository.saveAndFlush(documentSinister);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentSinister
        restDocumentSinisterMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentSinister.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentSinisterRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected DocumentSinister getPersistedDocumentSinister(DocumentSinister documentSinister) {
        return documentSinisterRepository.findById(documentSinister.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentSinisterToMatchAllProperties(DocumentSinister expectedDocumentSinister) {
        assertDocumentSinisterAllPropertiesEquals(expectedDocumentSinister, getPersistedDocumentSinister(expectedDocumentSinister));
    }

    protected void assertPersistedDocumentSinisterToMatchUpdatableProperties(DocumentSinister expectedDocumentSinister) {
        assertDocumentSinisterAllUpdatablePropertiesEquals(
            expectedDocumentSinister,
            getPersistedDocumentSinister(expectedDocumentSinister)
        );
    }
}
