package com.insurance.app.web.rest;

import static com.insurance.app.domain.VehicleUsageAsserts.*;
import static com.insurance.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.app.IntegrationTest;
import com.insurance.app.domain.VehicleUsage;
import com.insurance.app.repository.VehicleUsageRepository;
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
 * Integration tests for the {@link VehicleUsageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleUsageResourceIT {

    private static final String DEFAULT_USAGE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_USAGE_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANNUAL_MILEAGE = 1;
    private static final Integer UPDATED_ANNUAL_MILEAGE = 2;

    private static final Boolean DEFAULT_COMMERCIAL_USE = false;
    private static final Boolean UPDATED_COMMERCIAL_USE = true;

    private static final String ENTITY_API_URL = "/api/vehicle-usages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleUsageRepository vehicleUsageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleUsageMockMvc;

    private VehicleUsage vehicleUsage;

    private VehicleUsage insertedVehicleUsage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleUsage createEntity() {
        return new VehicleUsage().usageType(DEFAULT_USAGE_TYPE).annualMileage(DEFAULT_ANNUAL_MILEAGE).commercialUse(DEFAULT_COMMERCIAL_USE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleUsage createUpdatedEntity() {
        return new VehicleUsage().usageType(UPDATED_USAGE_TYPE).annualMileage(UPDATED_ANNUAL_MILEAGE).commercialUse(UPDATED_COMMERCIAL_USE);
    }

    @BeforeEach
    public void initTest() {
        vehicleUsage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleUsage != null) {
            vehicleUsageRepository.delete(insertedVehicleUsage);
            insertedVehicleUsage = null;
        }
    }

    @Test
    @Transactional
    void createVehicleUsage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleUsage
        var returnedVehicleUsage = om.readValue(
            restVehicleUsageMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleUsage))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleUsage.class
        );

        // Validate the VehicleUsage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVehicleUsageUpdatableFieldsEquals(returnedVehicleUsage, getPersistedVehicleUsage(returnedVehicleUsage));

        insertedVehicleUsage = returnedVehicleUsage;
    }

    @Test
    @Transactional
    void createVehicleUsageWithExistingId() throws Exception {
        // Create the VehicleUsage with an existing ID
        vehicleUsage.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleUsageMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleUsage)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVehicleUsages() throws Exception {
        // Initialize the database
        insertedVehicleUsage = vehicleUsageRepository.saveAndFlush(vehicleUsage);

        // Get all the vehicleUsageList
        restVehicleUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleUsage.getId().intValue())))
            .andExpect(jsonPath("$.[*].usageType").value(hasItem(DEFAULT_USAGE_TYPE)))
            .andExpect(jsonPath("$.[*].annualMileage").value(hasItem(DEFAULT_ANNUAL_MILEAGE)))
            .andExpect(jsonPath("$.[*].commercialUse").value(hasItem(DEFAULT_COMMERCIAL_USE)));
    }

    @Test
    @Transactional
    void getVehicleUsage() throws Exception {
        // Initialize the database
        insertedVehicleUsage = vehicleUsageRepository.saveAndFlush(vehicleUsage);

        // Get the vehicleUsage
        restVehicleUsageMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleUsage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleUsage.getId().intValue()))
            .andExpect(jsonPath("$.usageType").value(DEFAULT_USAGE_TYPE))
            .andExpect(jsonPath("$.annualMileage").value(DEFAULT_ANNUAL_MILEAGE))
            .andExpect(jsonPath("$.commercialUse").value(DEFAULT_COMMERCIAL_USE));
    }

    @Test
    @Transactional
    void getNonExistingVehicleUsage() throws Exception {
        // Get the vehicleUsage
        restVehicleUsageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleUsage() throws Exception {
        // Initialize the database
        insertedVehicleUsage = vehicleUsageRepository.saveAndFlush(vehicleUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleUsage
        VehicleUsage updatedVehicleUsage = vehicleUsageRepository.findById(vehicleUsage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleUsage are not directly saved in db
        em.detach(updatedVehicleUsage);
        updatedVehicleUsage.usageType(UPDATED_USAGE_TYPE).annualMileage(UPDATED_ANNUAL_MILEAGE).commercialUse(UPDATED_COMMERCIAL_USE);

        restVehicleUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicleUsage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVehicleUsage))
            )
            .andExpect(status().isOk());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleUsageToMatchAllProperties(updatedVehicleUsage);
    }

    @Test
    @Transactional
    void putNonExistingVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleUsage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleUsage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleUsageWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleUsage = vehicleUsageRepository.saveAndFlush(vehicleUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleUsage using partial update
        VehicleUsage partialUpdatedVehicleUsage = new VehicleUsage();
        partialUpdatedVehicleUsage.setId(vehicleUsage.getId());

        partialUpdatedVehicleUsage.usageType(UPDATED_USAGE_TYPE).annualMileage(UPDATED_ANNUAL_MILEAGE);

        restVehicleUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleUsage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleUsage))
            )
            .andExpect(status().isOk());

        // Validate the VehicleUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUsageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleUsage, vehicleUsage),
            getPersistedVehicleUsage(vehicleUsage)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleUsageWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleUsage = vehicleUsageRepository.saveAndFlush(vehicleUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleUsage using partial update
        VehicleUsage partialUpdatedVehicleUsage = new VehicleUsage();
        partialUpdatedVehicleUsage.setId(vehicleUsage.getId());

        partialUpdatedVehicleUsage
            .usageType(UPDATED_USAGE_TYPE)
            .annualMileage(UPDATED_ANNUAL_MILEAGE)
            .commercialUse(UPDATED_COMMERCIAL_USE);

        restVehicleUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleUsage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleUsage))
            )
            .andExpect(status().isOk());

        // Validate the VehicleUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUsageUpdatableFieldsEquals(partialUpdatedVehicleUsage, getPersistedVehicleUsage(partialUpdatedVehicleUsage));
    }

    @Test
    @Transactional
    void patchNonExistingVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleUsage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleUsage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleUsage() throws Exception {
        // Initialize the database
        insertedVehicleUsage = vehicleUsageRepository.saveAndFlush(vehicleUsage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleUsage
        restVehicleUsageMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleUsage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleUsageRepository.count();
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

    protected VehicleUsage getPersistedVehicleUsage(VehicleUsage vehicleUsage) {
        return vehicleUsageRepository.findById(vehicleUsage.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleUsageToMatchAllProperties(VehicleUsage expectedVehicleUsage) {
        assertVehicleUsageAllPropertiesEquals(expectedVehicleUsage, getPersistedVehicleUsage(expectedVehicleUsage));
    }

    protected void assertPersistedVehicleUsageToMatchUpdatableProperties(VehicleUsage expectedVehicleUsage) {
        assertVehicleUsageAllUpdatablePropertiesEquals(expectedVehicleUsage, getPersistedVehicleUsage(expectedVehicleUsage));
    }
}
