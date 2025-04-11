package com.insurance.app.web.rest;

import static com.insurance.app.domain.AccidentHistoryAsserts.*;
import static com.insurance.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.app.IntegrationTest;
import com.insurance.app.domain.AccidentHistory;
import com.insurance.app.repository.AccidentHistoryRepository;
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
 * Integration tests for the {@link AccidentHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccidentHistoryResourceIT {

    private static final String DEFAULT_ACCIDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ACCIDENT_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACCIDENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACCIDENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SEVERITY = "AAAAAAAAAA";
    private static final String UPDATED_SEVERITY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_REPAIR_COST = 1D;
    private static final Double UPDATED_REPAIR_COST = 2D;

    private static final String ENTITY_API_URL = "/api/accident-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccidentHistoryRepository accidentHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccidentHistoryMockMvc;

    private AccidentHistory accidentHistory;

    private AccidentHistory insertedAccidentHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccidentHistory createEntity() {
        return new AccidentHistory()
            .accidentId(DEFAULT_ACCIDENT_ID)
            .accidentDate(DEFAULT_ACCIDENT_DATE)
            .severity(DEFAULT_SEVERITY)
            .description(DEFAULT_DESCRIPTION)
            .repairCost(DEFAULT_REPAIR_COST);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccidentHistory createUpdatedEntity() {
        return new AccidentHistory()
            .accidentId(UPDATED_ACCIDENT_ID)
            .accidentDate(UPDATED_ACCIDENT_DATE)
            .severity(UPDATED_SEVERITY)
            .description(UPDATED_DESCRIPTION)
            .repairCost(UPDATED_REPAIR_COST);
    }

    @BeforeEach
    public void initTest() {
        accidentHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccidentHistory != null) {
            accidentHistoryRepository.delete(insertedAccidentHistory);
            insertedAccidentHistory = null;
        }
    }

    @Test
    @Transactional
    void createAccidentHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AccidentHistory
        var returnedAccidentHistory = om.readValue(
            restAccidentHistoryMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accidentHistory))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccidentHistory.class
        );

        // Validate the AccidentHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAccidentHistoryUpdatableFieldsEquals(returnedAccidentHistory, getPersistedAccidentHistory(returnedAccidentHistory));

        insertedAccidentHistory = returnedAccidentHistory;
    }

    @Test
    @Transactional
    void createAccidentHistoryWithExistingId() throws Exception {
        // Create the AccidentHistory with an existing ID
        accidentHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccidentHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accidentHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccidentHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccidentHistories() throws Exception {
        // Initialize the database
        insertedAccidentHistory = accidentHistoryRepository.saveAndFlush(accidentHistory);

        // Get all the accidentHistoryList
        restAccidentHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accidentHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].accidentId").value(hasItem(DEFAULT_ACCIDENT_ID)))
            .andExpect(jsonPath("$.[*].accidentDate").value(hasItem(DEFAULT_ACCIDENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].repairCost").value(hasItem(DEFAULT_REPAIR_COST)));
    }

    @Test
    @Transactional
    void getAccidentHistory() throws Exception {
        // Initialize the database
        insertedAccidentHistory = accidentHistoryRepository.saveAndFlush(accidentHistory);

        // Get the accidentHistory
        restAccidentHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, accidentHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accidentHistory.getId().intValue()))
            .andExpect(jsonPath("$.accidentId").value(DEFAULT_ACCIDENT_ID))
            .andExpect(jsonPath("$.accidentDate").value(DEFAULT_ACCIDENT_DATE.toString()))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.repairCost").value(DEFAULT_REPAIR_COST));
    }

    @Test
    @Transactional
    void getNonExistingAccidentHistory() throws Exception {
        // Get the accidentHistory
        restAccidentHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccidentHistory() throws Exception {
        // Initialize the database
        insertedAccidentHistory = accidentHistoryRepository.saveAndFlush(accidentHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accidentHistory
        AccidentHistory updatedAccidentHistory = accidentHistoryRepository.findById(accidentHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccidentHistory are not directly saved in db
        em.detach(updatedAccidentHistory);
        updatedAccidentHistory
            .accidentId(UPDATED_ACCIDENT_ID)
            .accidentDate(UPDATED_ACCIDENT_DATE)
            .severity(UPDATED_SEVERITY)
            .description(UPDATED_DESCRIPTION)
            .repairCost(UPDATED_REPAIR_COST);

        restAccidentHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccidentHistory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAccidentHistory))
            )
            .andExpect(status().isOk());

        // Validate the AccidentHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccidentHistoryToMatchAllProperties(updatedAccidentHistory);
    }

    @Test
    @Transactional
    void putNonExistingAccidentHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accidentHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccidentHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accidentHistory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accidentHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccidentHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccidentHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accidentHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccidentHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accidentHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccidentHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccidentHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accidentHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccidentHistoryMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accidentHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccidentHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccidentHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedAccidentHistory = accidentHistoryRepository.saveAndFlush(accidentHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accidentHistory using partial update
        AccidentHistory partialUpdatedAccidentHistory = new AccidentHistory();
        partialUpdatedAccidentHistory.setId(accidentHistory.getId());

        partialUpdatedAccidentHistory.accidentDate(UPDATED_ACCIDENT_DATE).severity(UPDATED_SEVERITY);

        restAccidentHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccidentHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccidentHistory))
            )
            .andExpect(status().isOk());

        // Validate the AccidentHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccidentHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccidentHistory, accidentHistory),
            getPersistedAccidentHistory(accidentHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccidentHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedAccidentHistory = accidentHistoryRepository.saveAndFlush(accidentHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accidentHistory using partial update
        AccidentHistory partialUpdatedAccidentHistory = new AccidentHistory();
        partialUpdatedAccidentHistory.setId(accidentHistory.getId());

        partialUpdatedAccidentHistory
            .accidentId(UPDATED_ACCIDENT_ID)
            .accidentDate(UPDATED_ACCIDENT_DATE)
            .severity(UPDATED_SEVERITY)
            .description(UPDATED_DESCRIPTION)
            .repairCost(UPDATED_REPAIR_COST);

        restAccidentHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccidentHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccidentHistory))
            )
            .andExpect(status().isOk());

        // Validate the AccidentHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccidentHistoryUpdatableFieldsEquals(
            partialUpdatedAccidentHistory,
            getPersistedAccidentHistory(partialUpdatedAccidentHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAccidentHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accidentHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccidentHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accidentHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accidentHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccidentHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccidentHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accidentHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccidentHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accidentHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccidentHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccidentHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accidentHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccidentHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accidentHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccidentHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccidentHistory() throws Exception {
        // Initialize the database
        insertedAccidentHistory = accidentHistoryRepository.saveAndFlush(accidentHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the accidentHistory
        restAccidentHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, accidentHistory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return accidentHistoryRepository.count();
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

    protected AccidentHistory getPersistedAccidentHistory(AccidentHistory accidentHistory) {
        return accidentHistoryRepository.findById(accidentHistory.getId()).orElseThrow();
    }

    protected void assertPersistedAccidentHistoryToMatchAllProperties(AccidentHistory expectedAccidentHistory) {
        assertAccidentHistoryAllPropertiesEquals(expectedAccidentHistory, getPersistedAccidentHistory(expectedAccidentHistory));
    }

    protected void assertPersistedAccidentHistoryToMatchUpdatableProperties(AccidentHistory expectedAccidentHistory) {
        assertAccidentHistoryAllUpdatablePropertiesEquals(expectedAccidentHistory, getPersistedAccidentHistory(expectedAccidentHistory));
    }
}
