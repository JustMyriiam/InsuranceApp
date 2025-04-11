package com.insurance.app.web.rest;

import static com.insurance.app.domain.LocationRiskAsserts.*;
import static com.insurance.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.app.IntegrationTest;
import com.insurance.app.domain.LocationRisk;
import com.insurance.app.repository.LocationRiskRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link LocationRiskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationRiskResourceIT {

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final Double DEFAULT_THEFT_RISK = 1D;
    private static final Double UPDATED_THEFT_RISK = 2D;

    private static final Double DEFAULT_ACCIDENT_RISK = 1D;
    private static final Double UPDATED_ACCIDENT_RISK = 2D;

    private static final Double DEFAULT_WEATHER_RISK = 1D;
    private static final Double UPDATED_WEATHER_RISK = 2D;

    private static final String ENTITY_API_URL = "/api/location-risks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocationRiskRepository locationRiskRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationRiskMockMvc;

    private LocationRisk locationRisk;

    private LocationRisk insertedLocationRisk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationRisk createEntity() {
        return new LocationRisk()
            .region(DEFAULT_REGION)
            .theftRisk(DEFAULT_THEFT_RISK)
            .accidentRisk(DEFAULT_ACCIDENT_RISK)
            .weatherRisk(DEFAULT_WEATHER_RISK);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationRisk createUpdatedEntity() {
        return new LocationRisk()
            .region(UPDATED_REGION)
            .theftRisk(UPDATED_THEFT_RISK)
            .accidentRisk(UPDATED_ACCIDENT_RISK)
            .weatherRisk(UPDATED_WEATHER_RISK);
    }

    @BeforeEach
    public void initTest() {
        locationRisk = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLocationRisk != null) {
            locationRiskRepository.delete(insertedLocationRisk);
            insertedLocationRisk = null;
        }
    }

    @Test
    @Transactional
    void createLocationRisk() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LocationRisk
        var returnedLocationRisk = om.readValue(
            restLocationRiskMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationRisk))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LocationRisk.class
        );

        // Validate the LocationRisk in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLocationRiskUpdatableFieldsEquals(returnedLocationRisk, getPersistedLocationRisk(returnedLocationRisk));

        insertedLocationRisk = returnedLocationRisk;
    }

    @Test
    @Transactional
    void createLocationRiskWithExistingId() throws Exception {
        // Create the LocationRisk with an existing ID
        locationRisk.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationRiskMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationRisk)))
            .andExpect(status().isBadRequest());

        // Validate the LocationRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocationRisks() throws Exception {
        // Initialize the database
        insertedLocationRisk = locationRiskRepository.saveAndFlush(locationRisk);

        // Get all the locationRiskList
        restLocationRiskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationRisk.getId().intValue())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].theftRisk").value(hasItem(DEFAULT_THEFT_RISK)))
            .andExpect(jsonPath("$.[*].accidentRisk").value(hasItem(DEFAULT_ACCIDENT_RISK)))
            .andExpect(jsonPath("$.[*].weatherRisk").value(hasItem(DEFAULT_WEATHER_RISK)));
    }

    @Test
    @Transactional
    void getLocationRisk() throws Exception {
        // Initialize the database
        insertedLocationRisk = locationRiskRepository.saveAndFlush(locationRisk);

        // Get the locationRisk
        restLocationRiskMockMvc
            .perform(get(ENTITY_API_URL_ID, locationRisk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationRisk.getId().intValue()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION))
            .andExpect(jsonPath("$.theftRisk").value(DEFAULT_THEFT_RISK))
            .andExpect(jsonPath("$.accidentRisk").value(DEFAULT_ACCIDENT_RISK))
            .andExpect(jsonPath("$.weatherRisk").value(DEFAULT_WEATHER_RISK));
    }

    @Test
    @Transactional
    void getNonExistingLocationRisk() throws Exception {
        // Get the locationRisk
        restLocationRiskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocationRisk() throws Exception {
        // Initialize the database
        insertedLocationRisk = locationRiskRepository.saveAndFlush(locationRisk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locationRisk
        LocationRisk updatedLocationRisk = locationRiskRepository.findById(locationRisk.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLocationRisk are not directly saved in db
        em.detach(updatedLocationRisk);
        updatedLocationRisk
            .region(UPDATED_REGION)
            .theftRisk(UPDATED_THEFT_RISK)
            .accidentRisk(UPDATED_ACCIDENT_RISK)
            .weatherRisk(UPDATED_WEATHER_RISK);

        restLocationRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocationRisk.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLocationRisk))
            )
            .andExpect(status().isOk());

        // Validate the LocationRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocationRiskToMatchAllProperties(updatedLocationRisk);
    }

    @Test
    @Transactional
    void putNonExistingLocationRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationRisk.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationRisk.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(locationRisk))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationRisk.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(locationRisk))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationRisk.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationRiskMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationRisk)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationRiskWithPatch() throws Exception {
        // Initialize the database
        insertedLocationRisk = locationRiskRepository.saveAndFlush(locationRisk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locationRisk using partial update
        LocationRisk partialUpdatedLocationRisk = new LocationRisk();
        partialUpdatedLocationRisk.setId(locationRisk.getId());

        partialUpdatedLocationRisk.theftRisk(UPDATED_THEFT_RISK);

        restLocationRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationRisk.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocationRisk))
            )
            .andExpect(status().isOk());

        // Validate the LocationRisk in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationRiskUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLocationRisk, locationRisk),
            getPersistedLocationRisk(locationRisk)
        );
    }

    @Test
    @Transactional
    void fullUpdateLocationRiskWithPatch() throws Exception {
        // Initialize the database
        insertedLocationRisk = locationRiskRepository.saveAndFlush(locationRisk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locationRisk using partial update
        LocationRisk partialUpdatedLocationRisk = new LocationRisk();
        partialUpdatedLocationRisk.setId(locationRisk.getId());

        partialUpdatedLocationRisk
            .region(UPDATED_REGION)
            .theftRisk(UPDATED_THEFT_RISK)
            .accidentRisk(UPDATED_ACCIDENT_RISK)
            .weatherRisk(UPDATED_WEATHER_RISK);

        restLocationRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationRisk.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocationRisk))
            )
            .andExpect(status().isOk());

        // Validate the LocationRisk in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationRiskUpdatableFieldsEquals(partialUpdatedLocationRisk, getPersistedLocationRisk(partialUpdatedLocationRisk));
    }

    @Test
    @Transactional
    void patchNonExistingLocationRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationRisk.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationRisk.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(locationRisk))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationRisk.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(locationRisk))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationRisk.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationRiskMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(locationRisk))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocationRisk() throws Exception {
        // Initialize the database
        insertedLocationRisk = locationRiskRepository.saveAndFlush(locationRisk);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the locationRisk
        restLocationRiskMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationRisk.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return locationRiskRepository.count();
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

    protected LocationRisk getPersistedLocationRisk(LocationRisk locationRisk) {
        return locationRiskRepository.findById(locationRisk.getId()).orElseThrow();
    }

    protected void assertPersistedLocationRiskToMatchAllProperties(LocationRisk expectedLocationRisk) {
        assertLocationRiskAllPropertiesEquals(expectedLocationRisk, getPersistedLocationRisk(expectedLocationRisk));
    }

    protected void assertPersistedLocationRiskToMatchUpdatableProperties(LocationRisk expectedLocationRisk) {
        assertLocationRiskAllUpdatablePropertiesEquals(expectedLocationRisk, getPersistedLocationRisk(expectedLocationRisk));
    }
}
