package com.insurance.app.web.rest;

import static com.insurance.app.domain.BurntStolenIncidentAsserts.*;
import static com.insurance.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.app.IntegrationTest;
import com.insurance.app.domain.BurntStolenIncident;
import com.insurance.app.repository.BurntStolenIncidentRepository;
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
 * Integration tests for the {@link BurntStolenIncidentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BurntStolenIncidentResourceIT {

    private static final String DEFAULT_INCIDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_INCIDENT_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_INCIDENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INCIDENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_ESTIMATED_LOSS = 1D;
    private static final Double UPDATED_ESTIMATED_LOSS = 2D;

    private static final String ENTITY_API_URL = "/api/burnt-stolen-incidents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BurntStolenIncidentRepository burntStolenIncidentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBurntStolenIncidentMockMvc;

    private BurntStolenIncident burntStolenIncident;

    private BurntStolenIncident insertedBurntStolenIncident;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BurntStolenIncident createEntity() {
        return new BurntStolenIncident()
            .incidentId(DEFAULT_INCIDENT_ID)
            .incidentDate(DEFAULT_INCIDENT_DATE)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .estimatedLoss(DEFAULT_ESTIMATED_LOSS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BurntStolenIncident createUpdatedEntity() {
        return new BurntStolenIncident()
            .incidentId(UPDATED_INCIDENT_ID)
            .incidentDate(UPDATED_INCIDENT_DATE)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .estimatedLoss(UPDATED_ESTIMATED_LOSS);
    }

    @BeforeEach
    public void initTest() {
        burntStolenIncident = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBurntStolenIncident != null) {
            burntStolenIncidentRepository.delete(insertedBurntStolenIncident);
            insertedBurntStolenIncident = null;
        }
    }

    @Test
    @Transactional
    void createBurntStolenIncident() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BurntStolenIncident
        var returnedBurntStolenIncident = om.readValue(
            restBurntStolenIncidentMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(burntStolenIncident))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BurntStolenIncident.class
        );

        // Validate the BurntStolenIncident in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBurntStolenIncidentUpdatableFieldsEquals(
            returnedBurntStolenIncident,
            getPersistedBurntStolenIncident(returnedBurntStolenIncident)
        );

        insertedBurntStolenIncident = returnedBurntStolenIncident;
    }

    @Test
    @Transactional
    void createBurntStolenIncidentWithExistingId() throws Exception {
        // Create the BurntStolenIncident with an existing ID
        burntStolenIncident.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBurntStolenIncidentMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(burntStolenIncident))
            )
            .andExpect(status().isBadRequest());

        // Validate the BurntStolenIncident in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBurntStolenIncidents() throws Exception {
        // Initialize the database
        insertedBurntStolenIncident = burntStolenIncidentRepository.saveAndFlush(burntStolenIncident);

        // Get all the burntStolenIncidentList
        restBurntStolenIncidentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(burntStolenIncident.getId().intValue())))
            .andExpect(jsonPath("$.[*].incidentId").value(hasItem(DEFAULT_INCIDENT_ID)))
            .andExpect(jsonPath("$.[*].incidentDate").value(hasItem(DEFAULT_INCIDENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].estimatedLoss").value(hasItem(DEFAULT_ESTIMATED_LOSS)));
    }

    @Test
    @Transactional
    void getBurntStolenIncident() throws Exception {
        // Initialize the database
        insertedBurntStolenIncident = burntStolenIncidentRepository.saveAndFlush(burntStolenIncident);

        // Get the burntStolenIncident
        restBurntStolenIncidentMockMvc
            .perform(get(ENTITY_API_URL_ID, burntStolenIncident.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(burntStolenIncident.getId().intValue()))
            .andExpect(jsonPath("$.incidentId").value(DEFAULT_INCIDENT_ID))
            .andExpect(jsonPath("$.incidentDate").value(DEFAULT_INCIDENT_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.estimatedLoss").value(DEFAULT_ESTIMATED_LOSS));
    }

    @Test
    @Transactional
    void getNonExistingBurntStolenIncident() throws Exception {
        // Get the burntStolenIncident
        restBurntStolenIncidentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBurntStolenIncident() throws Exception {
        // Initialize the database
        insertedBurntStolenIncident = burntStolenIncidentRepository.saveAndFlush(burntStolenIncident);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the burntStolenIncident
        BurntStolenIncident updatedBurntStolenIncident = burntStolenIncidentRepository.findById(burntStolenIncident.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBurntStolenIncident are not directly saved in db
        em.detach(updatedBurntStolenIncident);
        updatedBurntStolenIncident
            .incidentId(UPDATED_INCIDENT_ID)
            .incidentDate(UPDATED_INCIDENT_DATE)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .estimatedLoss(UPDATED_ESTIMATED_LOSS);

        restBurntStolenIncidentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBurntStolenIncident.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBurntStolenIncident))
            )
            .andExpect(status().isOk());

        // Validate the BurntStolenIncident in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBurntStolenIncidentToMatchAllProperties(updatedBurntStolenIncident);
    }

    @Test
    @Transactional
    void putNonExistingBurntStolenIncident() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        burntStolenIncident.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBurntStolenIncidentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, burntStolenIncident.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(burntStolenIncident))
            )
            .andExpect(status().isBadRequest());

        // Validate the BurntStolenIncident in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBurntStolenIncident() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        burntStolenIncident.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBurntStolenIncidentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(burntStolenIncident))
            )
            .andExpect(status().isBadRequest());

        // Validate the BurntStolenIncident in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBurntStolenIncident() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        burntStolenIncident.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBurntStolenIncidentMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(burntStolenIncident))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BurntStolenIncident in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBurntStolenIncidentWithPatch() throws Exception {
        // Initialize the database
        insertedBurntStolenIncident = burntStolenIncidentRepository.saveAndFlush(burntStolenIncident);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the burntStolenIncident using partial update
        BurntStolenIncident partialUpdatedBurntStolenIncident = new BurntStolenIncident();
        partialUpdatedBurntStolenIncident.setId(burntStolenIncident.getId());

        partialUpdatedBurntStolenIncident.incidentId(UPDATED_INCIDENT_ID).type(UPDATED_TYPE).estimatedLoss(UPDATED_ESTIMATED_LOSS);

        restBurntStolenIncidentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBurntStolenIncident.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBurntStolenIncident))
            )
            .andExpect(status().isOk());

        // Validate the BurntStolenIncident in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBurntStolenIncidentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBurntStolenIncident, burntStolenIncident),
            getPersistedBurntStolenIncident(burntStolenIncident)
        );
    }

    @Test
    @Transactional
    void fullUpdateBurntStolenIncidentWithPatch() throws Exception {
        // Initialize the database
        insertedBurntStolenIncident = burntStolenIncidentRepository.saveAndFlush(burntStolenIncident);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the burntStolenIncident using partial update
        BurntStolenIncident partialUpdatedBurntStolenIncident = new BurntStolenIncident();
        partialUpdatedBurntStolenIncident.setId(burntStolenIncident.getId());

        partialUpdatedBurntStolenIncident
            .incidentId(UPDATED_INCIDENT_ID)
            .incidentDate(UPDATED_INCIDENT_DATE)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .estimatedLoss(UPDATED_ESTIMATED_LOSS);

        restBurntStolenIncidentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBurntStolenIncident.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBurntStolenIncident))
            )
            .andExpect(status().isOk());

        // Validate the BurntStolenIncident in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBurntStolenIncidentUpdatableFieldsEquals(
            partialUpdatedBurntStolenIncident,
            getPersistedBurntStolenIncident(partialUpdatedBurntStolenIncident)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBurntStolenIncident() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        burntStolenIncident.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBurntStolenIncidentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, burntStolenIncident.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(burntStolenIncident))
            )
            .andExpect(status().isBadRequest());

        // Validate the BurntStolenIncident in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBurntStolenIncident() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        burntStolenIncident.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBurntStolenIncidentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(burntStolenIncident))
            )
            .andExpect(status().isBadRequest());

        // Validate the BurntStolenIncident in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBurntStolenIncident() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        burntStolenIncident.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBurntStolenIncidentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(burntStolenIncident))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BurntStolenIncident in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBurntStolenIncident() throws Exception {
        // Initialize the database
        insertedBurntStolenIncident = burntStolenIncidentRepository.saveAndFlush(burntStolenIncident);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the burntStolenIncident
        restBurntStolenIncidentMockMvc
            .perform(delete(ENTITY_API_URL_ID, burntStolenIncident.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return burntStolenIncidentRepository.count();
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

    protected BurntStolenIncident getPersistedBurntStolenIncident(BurntStolenIncident burntStolenIncident) {
        return burntStolenIncidentRepository.findById(burntStolenIncident.getId()).orElseThrow();
    }

    protected void assertPersistedBurntStolenIncidentToMatchAllProperties(BurntStolenIncident expectedBurntStolenIncident) {
        assertBurntStolenIncidentAllPropertiesEquals(
            expectedBurntStolenIncident,
            getPersistedBurntStolenIncident(expectedBurntStolenIncident)
        );
    }

    protected void assertPersistedBurntStolenIncidentToMatchUpdatableProperties(BurntStolenIncident expectedBurntStolenIncident) {
        assertBurntStolenIncidentAllUpdatablePropertiesEquals(
            expectedBurntStolenIncident,
            getPersistedBurntStolenIncident(expectedBurntStolenIncident)
        );
    }
}
