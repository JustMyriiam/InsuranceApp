package com.insurance.app.web.rest;

import static com.insurance.app.domain.VehicleAccessoryAsserts.*;
import static com.insurance.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.app.IntegrationTest;
import com.insurance.app.domain.VehicleAccessory;
import com.insurance.app.repository.VehicleAccessoryRepository;
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
 * Integration tests for the {@link VehicleAccessoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleAccessoryResourceIT {

    private static final String DEFAULT_ACCESSORY_ID = "AAAAAAAAAA";
    private static final String UPDATED_ACCESSORY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FACTORY_INSTALLED = false;
    private static final Boolean UPDATED_FACTORY_INSTALLED = true;

    private static final String ENTITY_API_URL = "/api/vehicle-accessories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleAccessoryRepository vehicleAccessoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleAccessoryMockMvc;

    private VehicleAccessory vehicleAccessory;

    private VehicleAccessory insertedVehicleAccessory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleAccessory createEntity() {
        return new VehicleAccessory()
            .accessoryId(DEFAULT_ACCESSORY_ID)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .factoryInstalled(DEFAULT_FACTORY_INSTALLED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleAccessory createUpdatedEntity() {
        return new VehicleAccessory()
            .accessoryId(UPDATED_ACCESSORY_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .factoryInstalled(UPDATED_FACTORY_INSTALLED);
    }

    @BeforeEach
    public void initTest() {
        vehicleAccessory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleAccessory != null) {
            vehicleAccessoryRepository.delete(insertedVehicleAccessory);
            insertedVehicleAccessory = null;
        }
    }

    @Test
    @Transactional
    void createVehicleAccessory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleAccessory
        var returnedVehicleAccessory = om.readValue(
            restVehicleAccessoryMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(vehicleAccessory))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleAccessory.class
        );

        // Validate the VehicleAccessory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVehicleAccessoryUpdatableFieldsEquals(returnedVehicleAccessory, getPersistedVehicleAccessory(returnedVehicleAccessory));

        insertedVehicleAccessory = returnedVehicleAccessory;
    }

    @Test
    @Transactional
    void createVehicleAccessoryWithExistingId() throws Exception {
        // Create the VehicleAccessory with an existing ID
        vehicleAccessory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleAccessoryMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleAccessory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleAccessory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVehicleAccessories() throws Exception {
        // Initialize the database
        insertedVehicleAccessory = vehicleAccessoryRepository.saveAndFlush(vehicleAccessory);

        // Get all the vehicleAccessoryList
        restVehicleAccessoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleAccessory.getId().intValue())))
            .andExpect(jsonPath("$.[*].accessoryId").value(hasItem(DEFAULT_ACCESSORY_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].factoryInstalled").value(hasItem(DEFAULT_FACTORY_INSTALLED)));
    }

    @Test
    @Transactional
    void getVehicleAccessory() throws Exception {
        // Initialize the database
        insertedVehicleAccessory = vehicleAccessoryRepository.saveAndFlush(vehicleAccessory);

        // Get the vehicleAccessory
        restVehicleAccessoryMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleAccessory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleAccessory.getId().intValue()))
            .andExpect(jsonPath("$.accessoryId").value(DEFAULT_ACCESSORY_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.factoryInstalled").value(DEFAULT_FACTORY_INSTALLED));
    }

    @Test
    @Transactional
    void getNonExistingVehicleAccessory() throws Exception {
        // Get the vehicleAccessory
        restVehicleAccessoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleAccessory() throws Exception {
        // Initialize the database
        insertedVehicleAccessory = vehicleAccessoryRepository.saveAndFlush(vehicleAccessory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleAccessory
        VehicleAccessory updatedVehicleAccessory = vehicleAccessoryRepository.findById(vehicleAccessory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleAccessory are not directly saved in db
        em.detach(updatedVehicleAccessory);
        updatedVehicleAccessory
            .accessoryId(UPDATED_ACCESSORY_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .factoryInstalled(UPDATED_FACTORY_INSTALLED);

        restVehicleAccessoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicleAccessory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVehicleAccessory))
            )
            .andExpect(status().isOk());

        // Validate the VehicleAccessory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleAccessoryToMatchAllProperties(updatedVehicleAccessory);
    }

    @Test
    @Transactional
    void putNonExistingVehicleAccessory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAccessory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleAccessoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleAccessory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleAccessory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleAccessory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleAccessory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAccessory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleAccessoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleAccessory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleAccessory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleAccessory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAccessory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleAccessoryMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleAccessory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleAccessory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleAccessoryWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleAccessory = vehicleAccessoryRepository.saveAndFlush(vehicleAccessory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleAccessory using partial update
        VehicleAccessory partialUpdatedVehicleAccessory = new VehicleAccessory();
        partialUpdatedVehicleAccessory.setId(vehicleAccessory.getId());

        partialUpdatedVehicleAccessory.name(UPDATED_NAME).type(UPDATED_TYPE).factoryInstalled(UPDATED_FACTORY_INSTALLED);

        restVehicleAccessoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleAccessory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleAccessory))
            )
            .andExpect(status().isOk());

        // Validate the VehicleAccessory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleAccessoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleAccessory, vehicleAccessory),
            getPersistedVehicleAccessory(vehicleAccessory)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleAccessoryWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleAccessory = vehicleAccessoryRepository.saveAndFlush(vehicleAccessory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleAccessory using partial update
        VehicleAccessory partialUpdatedVehicleAccessory = new VehicleAccessory();
        partialUpdatedVehicleAccessory.setId(vehicleAccessory.getId());

        partialUpdatedVehicleAccessory
            .accessoryId(UPDATED_ACCESSORY_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .factoryInstalled(UPDATED_FACTORY_INSTALLED);

        restVehicleAccessoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleAccessory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleAccessory))
            )
            .andExpect(status().isOk());

        // Validate the VehicleAccessory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleAccessoryUpdatableFieldsEquals(
            partialUpdatedVehicleAccessory,
            getPersistedVehicleAccessory(partialUpdatedVehicleAccessory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingVehicleAccessory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAccessory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleAccessoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleAccessory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleAccessory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleAccessory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleAccessory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAccessory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleAccessoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleAccessory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleAccessory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleAccessory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAccessory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleAccessoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleAccessory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleAccessory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleAccessory() throws Exception {
        // Initialize the database
        insertedVehicleAccessory = vehicleAccessoryRepository.saveAndFlush(vehicleAccessory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleAccessory
        restVehicleAccessoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleAccessory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleAccessoryRepository.count();
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

    protected VehicleAccessory getPersistedVehicleAccessory(VehicleAccessory vehicleAccessory) {
        return vehicleAccessoryRepository.findById(vehicleAccessory.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleAccessoryToMatchAllProperties(VehicleAccessory expectedVehicleAccessory) {
        assertVehicleAccessoryAllPropertiesEquals(expectedVehicleAccessory, getPersistedVehicleAccessory(expectedVehicleAccessory));
    }

    protected void assertPersistedVehicleAccessoryToMatchUpdatableProperties(VehicleAccessory expectedVehicleAccessory) {
        assertVehicleAccessoryAllUpdatablePropertiesEquals(
            expectedVehicleAccessory,
            getPersistedVehicleAccessory(expectedVehicleAccessory)
        );
    }
}
