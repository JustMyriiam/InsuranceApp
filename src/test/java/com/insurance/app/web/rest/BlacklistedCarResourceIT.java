package com.insurance.app.web.rest;

import static com.insurance.app.domain.BlacklistedCarAsserts.*;
import static com.insurance.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.app.IntegrationTest;
import com.insurance.app.domain.BlacklistedCar;
import com.insurance.app.repository.BlacklistedCarRepository;
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
 * Integration tests for the {@link BlacklistedCarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BlacklistedCarResourceIT {

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_BLACKLIST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BLACKLIST_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/blacklisted-cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BlacklistedCarRepository blacklistedCarRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlacklistedCarMockMvc;

    private BlacklistedCar blacklistedCar;

    private BlacklistedCar insertedBlacklistedCar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlacklistedCar createEntity() {
        return new BlacklistedCar().reason(DEFAULT_REASON).blacklistDate(DEFAULT_BLACKLIST_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlacklistedCar createUpdatedEntity() {
        return new BlacklistedCar().reason(UPDATED_REASON).blacklistDate(UPDATED_BLACKLIST_DATE);
    }

    @BeforeEach
    public void initTest() {
        blacklistedCar = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBlacklistedCar != null) {
            blacklistedCarRepository.delete(insertedBlacklistedCar);
            insertedBlacklistedCar = null;
        }
    }

    @Test
    @Transactional
    void createBlacklistedCar() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BlacklistedCar
        var returnedBlacklistedCar = om.readValue(
            restBlacklistedCarMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blacklistedCar))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BlacklistedCar.class
        );

        // Validate the BlacklistedCar in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBlacklistedCarUpdatableFieldsEquals(returnedBlacklistedCar, getPersistedBlacklistedCar(returnedBlacklistedCar));

        insertedBlacklistedCar = returnedBlacklistedCar;
    }

    @Test
    @Transactional
    void createBlacklistedCarWithExistingId() throws Exception {
        // Create the BlacklistedCar with an existing ID
        blacklistedCar.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlacklistedCarMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blacklistedCar))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlacklistedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBlacklistedCars() throws Exception {
        // Initialize the database
        insertedBlacklistedCar = blacklistedCarRepository.saveAndFlush(blacklistedCar);

        // Get all the blacklistedCarList
        restBlacklistedCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blacklistedCar.getId().intValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].blacklistDate").value(hasItem(DEFAULT_BLACKLIST_DATE.toString())));
    }

    @Test
    @Transactional
    void getBlacklistedCar() throws Exception {
        // Initialize the database
        insertedBlacklistedCar = blacklistedCarRepository.saveAndFlush(blacklistedCar);

        // Get the blacklistedCar
        restBlacklistedCarMockMvc
            .perform(get(ENTITY_API_URL_ID, blacklistedCar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(blacklistedCar.getId().intValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.blacklistDate").value(DEFAULT_BLACKLIST_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBlacklistedCar() throws Exception {
        // Get the blacklistedCar
        restBlacklistedCarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBlacklistedCar() throws Exception {
        // Initialize the database
        insertedBlacklistedCar = blacklistedCarRepository.saveAndFlush(blacklistedCar);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blacklistedCar
        BlacklistedCar updatedBlacklistedCar = blacklistedCarRepository.findById(blacklistedCar.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBlacklistedCar are not directly saved in db
        em.detach(updatedBlacklistedCar);
        updatedBlacklistedCar.reason(UPDATED_REASON).blacklistDate(UPDATED_BLACKLIST_DATE);

        restBlacklistedCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBlacklistedCar.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBlacklistedCar))
            )
            .andExpect(status().isOk());

        // Validate the BlacklistedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBlacklistedCarToMatchAllProperties(updatedBlacklistedCar);
    }

    @Test
    @Transactional
    void putNonExistingBlacklistedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blacklistedCar.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlacklistedCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blacklistedCar.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blacklistedCar))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlacklistedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBlacklistedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blacklistedCar.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlacklistedCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blacklistedCar))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlacklistedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBlacklistedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blacklistedCar.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlacklistedCarMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blacklistedCar)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlacklistedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBlacklistedCarWithPatch() throws Exception {
        // Initialize the database
        insertedBlacklistedCar = blacklistedCarRepository.saveAndFlush(blacklistedCar);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blacklistedCar using partial update
        BlacklistedCar partialUpdatedBlacklistedCar = new BlacklistedCar();
        partialUpdatedBlacklistedCar.setId(blacklistedCar.getId());

        partialUpdatedBlacklistedCar.reason(UPDATED_REASON);

        restBlacklistedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlacklistedCar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlacklistedCar))
            )
            .andExpect(status().isOk());

        // Validate the BlacklistedCar in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlacklistedCarUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBlacklistedCar, blacklistedCar),
            getPersistedBlacklistedCar(blacklistedCar)
        );
    }

    @Test
    @Transactional
    void fullUpdateBlacklistedCarWithPatch() throws Exception {
        // Initialize the database
        insertedBlacklistedCar = blacklistedCarRepository.saveAndFlush(blacklistedCar);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blacklistedCar using partial update
        BlacklistedCar partialUpdatedBlacklistedCar = new BlacklistedCar();
        partialUpdatedBlacklistedCar.setId(blacklistedCar.getId());

        partialUpdatedBlacklistedCar.reason(UPDATED_REASON).blacklistDate(UPDATED_BLACKLIST_DATE);

        restBlacklistedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlacklistedCar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlacklistedCar))
            )
            .andExpect(status().isOk());

        // Validate the BlacklistedCar in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlacklistedCarUpdatableFieldsEquals(partialUpdatedBlacklistedCar, getPersistedBlacklistedCar(partialUpdatedBlacklistedCar));
    }

    @Test
    @Transactional
    void patchNonExistingBlacklistedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blacklistedCar.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlacklistedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, blacklistedCar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(blacklistedCar))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlacklistedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBlacklistedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blacklistedCar.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlacklistedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(blacklistedCar))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlacklistedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBlacklistedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blacklistedCar.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlacklistedCarMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(blacklistedCar))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlacklistedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBlacklistedCar() throws Exception {
        // Initialize the database
        insertedBlacklistedCar = blacklistedCarRepository.saveAndFlush(blacklistedCar);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the blacklistedCar
        restBlacklistedCarMockMvc
            .perform(delete(ENTITY_API_URL_ID, blacklistedCar.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return blacklistedCarRepository.count();
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

    protected BlacklistedCar getPersistedBlacklistedCar(BlacklistedCar blacklistedCar) {
        return blacklistedCarRepository.findById(blacklistedCar.getId()).orElseThrow();
    }

    protected void assertPersistedBlacklistedCarToMatchAllProperties(BlacklistedCar expectedBlacklistedCar) {
        assertBlacklistedCarAllPropertiesEquals(expectedBlacklistedCar, getPersistedBlacklistedCar(expectedBlacklistedCar));
    }

    protected void assertPersistedBlacklistedCarToMatchUpdatableProperties(BlacklistedCar expectedBlacklistedCar) {
        assertBlacklistedCarAllUpdatablePropertiesEquals(expectedBlacklistedCar, getPersistedBlacklistedCar(expectedBlacklistedCar));
    }
}
