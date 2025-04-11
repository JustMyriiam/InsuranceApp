package com.insurance.app.web.rest;

import static com.insurance.app.domain.ParkingAsserts.*;
import static com.insurance.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.app.IntegrationTest;
import com.insurance.app.domain.Parking;
import com.insurance.app.repository.ParkingRepository;
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
 * Integration tests for the {@link ParkingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParkingResourceIT {

    private static final String DEFAULT_PARKING_ID = "AAAAAAAAAA";
    private static final String UPDATED_PARKING_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SECURED = false;
    private static final Boolean UPDATED_IS_SECURED = true;

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    private static final String ENTITY_API_URL = "/api/parkings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParkingMockMvc;

    private Parking parking;

    private Parking insertedParking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parking createEntity() {
        return new Parking()
            .parkingId(DEFAULT_PARKING_ID)
            .location(DEFAULT_LOCATION)
            .isSecured(DEFAULT_IS_SECURED)
            .capacity(DEFAULT_CAPACITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parking createUpdatedEntity() {
        return new Parking()
            .parkingId(UPDATED_PARKING_ID)
            .location(UPDATED_LOCATION)
            .isSecured(UPDATED_IS_SECURED)
            .capacity(UPDATED_CAPACITY);
    }

    @BeforeEach
    public void initTest() {
        parking = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedParking != null) {
            parkingRepository.delete(insertedParking);
            insertedParking = null;
        }
    }

    @Test
    @Transactional
    void createParking() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Parking
        var returnedParking = om.readValue(
            restParkingMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parking)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Parking.class
        );

        // Validate the Parking in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertParkingUpdatableFieldsEquals(returnedParking, getPersistedParking(returnedParking));

        insertedParking = returnedParking;
    }

    @Test
    @Transactional
    void createParkingWithExistingId() throws Exception {
        // Create the Parking with an existing ID
        parking.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParkingMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parking)))
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParkings() throws Exception {
        // Initialize the database
        insertedParking = parkingRepository.saveAndFlush(parking);

        // Get all the parkingList
        restParkingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parking.getId().intValue())))
            .andExpect(jsonPath("$.[*].parkingId").value(hasItem(DEFAULT_PARKING_ID)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].isSecured").value(hasItem(DEFAULT_IS_SECURED)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)));
    }

    @Test
    @Transactional
    void getParking() throws Exception {
        // Initialize the database
        insertedParking = parkingRepository.saveAndFlush(parking);

        // Get the parking
        restParkingMockMvc
            .perform(get(ENTITY_API_URL_ID, parking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parking.getId().intValue()))
            .andExpect(jsonPath("$.parkingId").value(DEFAULT_PARKING_ID))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.isSecured").value(DEFAULT_IS_SECURED))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY));
    }

    @Test
    @Transactional
    void getNonExistingParking() throws Exception {
        // Get the parking
        restParkingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParking() throws Exception {
        // Initialize the database
        insertedParking = parkingRepository.saveAndFlush(parking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parking
        Parking updatedParking = parkingRepository.findById(parking.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedParking are not directly saved in db
        em.detach(updatedParking);
        updatedParking.parkingId(UPDATED_PARKING_ID).location(UPDATED_LOCATION).isSecured(UPDATED_IS_SECURED).capacity(UPDATED_CAPACITY);

        restParkingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParking.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedParking))
            )
            .andExpect(status().isOk());

        // Validate the Parking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedParkingToMatchAllProperties(updatedParking);
    }

    @Test
    @Transactional
    void putNonExistingParking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parking.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parking.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(parking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parking.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(parking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parking.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parking)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParkingWithPatch() throws Exception {
        // Initialize the database
        insertedParking = parkingRepository.saveAndFlush(parking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parking using partial update
        Parking partialUpdatedParking = new Parking();
        partialUpdatedParking.setId(parking.getId());

        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParking.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParking))
            )
            .andExpect(status().isOk());

        // Validate the Parking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParkingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedParking, parking), getPersistedParking(parking));
    }

    @Test
    @Transactional
    void fullUpdateParkingWithPatch() throws Exception {
        // Initialize the database
        insertedParking = parkingRepository.saveAndFlush(parking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parking using partial update
        Parking partialUpdatedParking = new Parking();
        partialUpdatedParking.setId(parking.getId());

        partialUpdatedParking
            .parkingId(UPDATED_PARKING_ID)
            .location(UPDATED_LOCATION)
            .isSecured(UPDATED_IS_SECURED)
            .capacity(UPDATED_CAPACITY);

        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParking.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParking))
            )
            .andExpect(status().isOk());

        // Validate the Parking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParkingUpdatableFieldsEquals(partialUpdatedParking, getPersistedParking(partialUpdatedParking));
    }

    @Test
    @Transactional
    void patchNonExistingParking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parking.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parking.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(parking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parking.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(parking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parking.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(parking)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParking() throws Exception {
        // Initialize the database
        insertedParking = parkingRepository.saveAndFlush(parking);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the parking
        restParkingMockMvc
            .perform(delete(ENTITY_API_URL_ID, parking.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return parkingRepository.count();
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

    protected Parking getPersistedParking(Parking parking) {
        return parkingRepository.findById(parking.getId()).orElseThrow();
    }

    protected void assertPersistedParkingToMatchAllProperties(Parking expectedParking) {
        assertParkingAllPropertiesEquals(expectedParking, getPersistedParking(expectedParking));
    }

    protected void assertPersistedParkingToMatchUpdatableProperties(Parking expectedParking) {
        assertParkingAllUpdatablePropertiesEquals(expectedParking, getPersistedParking(expectedParking));
    }
}
