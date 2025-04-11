package com.insurance.app.web.rest;

import static com.insurance.app.domain.TrafficViolationAsserts.*;
import static com.insurance.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.app.IntegrationTest;
import com.insurance.app.domain.TrafficViolation;
import com.insurance.app.repository.TrafficViolationRepository;
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
 * Integration tests for the {@link TrafficViolationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrafficViolationResourceIT {

    private static final String DEFAULT_VIOLATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VIOLATION_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_VIOLATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VIOLATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PENALTY_POINTS = 1D;
    private static final Double UPDATED_PENALTY_POINTS = 2D;

    private static final String ENTITY_API_URL = "/api/traffic-violations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrafficViolationRepository trafficViolationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrafficViolationMockMvc;

    private TrafficViolation trafficViolation;

    private TrafficViolation insertedTrafficViolation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrafficViolation createEntity() {
        return new TrafficViolation()
            .violationType(DEFAULT_VIOLATION_TYPE)
            .violationDate(DEFAULT_VIOLATION_DATE)
            .penaltyPoints(DEFAULT_PENALTY_POINTS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrafficViolation createUpdatedEntity() {
        return new TrafficViolation()
            .violationType(UPDATED_VIOLATION_TYPE)
            .violationDate(UPDATED_VIOLATION_DATE)
            .penaltyPoints(UPDATED_PENALTY_POINTS);
    }

    @BeforeEach
    public void initTest() {
        trafficViolation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTrafficViolation != null) {
            trafficViolationRepository.delete(insertedTrafficViolation);
            insertedTrafficViolation = null;
        }
    }

    @Test
    @Transactional
    void createTrafficViolation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrafficViolation
        var returnedTrafficViolation = om.readValue(
            restTrafficViolationMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(trafficViolation))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrafficViolation.class
        );

        // Validate the TrafficViolation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTrafficViolationUpdatableFieldsEquals(returnedTrafficViolation, getPersistedTrafficViolation(returnedTrafficViolation));

        insertedTrafficViolation = returnedTrafficViolation;
    }

    @Test
    @Transactional
    void createTrafficViolationWithExistingId() throws Exception {
        // Create the TrafficViolation with an existing ID
        trafficViolation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrafficViolationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trafficViolation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrafficViolation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrafficViolations() throws Exception {
        // Initialize the database
        insertedTrafficViolation = trafficViolationRepository.saveAndFlush(trafficViolation);

        // Get all the trafficViolationList
        restTrafficViolationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trafficViolation.getId().intValue())))
            .andExpect(jsonPath("$.[*].violationType").value(hasItem(DEFAULT_VIOLATION_TYPE)))
            .andExpect(jsonPath("$.[*].violationDate").value(hasItem(DEFAULT_VIOLATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].penaltyPoints").value(hasItem(DEFAULT_PENALTY_POINTS)));
    }

    @Test
    @Transactional
    void getTrafficViolation() throws Exception {
        // Initialize the database
        insertedTrafficViolation = trafficViolationRepository.saveAndFlush(trafficViolation);

        // Get the trafficViolation
        restTrafficViolationMockMvc
            .perform(get(ENTITY_API_URL_ID, trafficViolation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trafficViolation.getId().intValue()))
            .andExpect(jsonPath("$.violationType").value(DEFAULT_VIOLATION_TYPE))
            .andExpect(jsonPath("$.violationDate").value(DEFAULT_VIOLATION_DATE.toString()))
            .andExpect(jsonPath("$.penaltyPoints").value(DEFAULT_PENALTY_POINTS));
    }

    @Test
    @Transactional
    void getNonExistingTrafficViolation() throws Exception {
        // Get the trafficViolation
        restTrafficViolationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrafficViolation() throws Exception {
        // Initialize the database
        insertedTrafficViolation = trafficViolationRepository.saveAndFlush(trafficViolation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trafficViolation
        TrafficViolation updatedTrafficViolation = trafficViolationRepository.findById(trafficViolation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrafficViolation are not directly saved in db
        em.detach(updatedTrafficViolation);
        updatedTrafficViolation
            .violationType(UPDATED_VIOLATION_TYPE)
            .violationDate(UPDATED_VIOLATION_DATE)
            .penaltyPoints(UPDATED_PENALTY_POINTS);

        restTrafficViolationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrafficViolation.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTrafficViolation))
            )
            .andExpect(status().isOk());

        // Validate the TrafficViolation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrafficViolationToMatchAllProperties(updatedTrafficViolation);
    }

    @Test
    @Transactional
    void putNonExistingTrafficViolation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trafficViolation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrafficViolationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trafficViolation.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trafficViolation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrafficViolation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrafficViolation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trafficViolation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrafficViolationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trafficViolation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrafficViolation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrafficViolation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trafficViolation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrafficViolationMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trafficViolation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrafficViolation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrafficViolationWithPatch() throws Exception {
        // Initialize the database
        insertedTrafficViolation = trafficViolationRepository.saveAndFlush(trafficViolation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trafficViolation using partial update
        TrafficViolation partialUpdatedTrafficViolation = new TrafficViolation();
        partialUpdatedTrafficViolation.setId(trafficViolation.getId());

        partialUpdatedTrafficViolation.violationType(UPDATED_VIOLATION_TYPE).violationDate(UPDATED_VIOLATION_DATE);

        restTrafficViolationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrafficViolation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrafficViolation))
            )
            .andExpect(status().isOk());

        // Validate the TrafficViolation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrafficViolationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrafficViolation, trafficViolation),
            getPersistedTrafficViolation(trafficViolation)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrafficViolationWithPatch() throws Exception {
        // Initialize the database
        insertedTrafficViolation = trafficViolationRepository.saveAndFlush(trafficViolation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trafficViolation using partial update
        TrafficViolation partialUpdatedTrafficViolation = new TrafficViolation();
        partialUpdatedTrafficViolation.setId(trafficViolation.getId());

        partialUpdatedTrafficViolation
            .violationType(UPDATED_VIOLATION_TYPE)
            .violationDate(UPDATED_VIOLATION_DATE)
            .penaltyPoints(UPDATED_PENALTY_POINTS);

        restTrafficViolationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrafficViolation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrafficViolation))
            )
            .andExpect(status().isOk());

        // Validate the TrafficViolation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrafficViolationUpdatableFieldsEquals(
            partialUpdatedTrafficViolation,
            getPersistedTrafficViolation(partialUpdatedTrafficViolation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTrafficViolation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trafficViolation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrafficViolationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trafficViolation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trafficViolation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrafficViolation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrafficViolation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trafficViolation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrafficViolationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trafficViolation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrafficViolation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrafficViolation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trafficViolation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrafficViolationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trafficViolation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrafficViolation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrafficViolation() throws Exception {
        // Initialize the database
        insertedTrafficViolation = trafficViolationRepository.saveAndFlush(trafficViolation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trafficViolation
        restTrafficViolationMockMvc
            .perform(delete(ENTITY_API_URL_ID, trafficViolation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trafficViolationRepository.count();
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

    protected TrafficViolation getPersistedTrafficViolation(TrafficViolation trafficViolation) {
        return trafficViolationRepository.findById(trafficViolation.getId()).orElseThrow();
    }

    protected void assertPersistedTrafficViolationToMatchAllProperties(TrafficViolation expectedTrafficViolation) {
        assertTrafficViolationAllPropertiesEquals(expectedTrafficViolation, getPersistedTrafficViolation(expectedTrafficViolation));
    }

    protected void assertPersistedTrafficViolationToMatchUpdatableProperties(TrafficViolation expectedTrafficViolation) {
        assertTrafficViolationAllUpdatablePropertiesEquals(
            expectedTrafficViolation,
            getPersistedTrafficViolation(expectedTrafficViolation)
        );
    }
}
