package com.insurance.app.web.rest;

import static com.insurance.app.domain.InsuranceOfferAsserts.*;
import static com.insurance.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.app.IntegrationTest;
import com.insurance.app.domain.InsuranceOffer;
import com.insurance.app.repository.InsuranceOfferRepository;
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
 * Integration tests for the {@link InsuranceOfferResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceOfferResourceIT {

    private static final String DEFAULT_OFFER_ID = "AAAAAAAAAA";
    private static final String UPDATED_OFFER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_OFFER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OFFER_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_COVERAGE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_COVERAGE_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_TERMS_AND_CONDITIONS = "AAAAAAAAAA";
    private static final String UPDATED_TERMS_AND_CONDITIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/insurance-offers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsuranceOfferRepository insuranceOfferRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceOfferMockMvc;

    private InsuranceOffer insuranceOffer;

    private InsuranceOffer insertedInsuranceOffer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceOffer createEntity() {
        return new InsuranceOffer()
            .offerId(DEFAULT_OFFER_ID)
            .offerName(DEFAULT_OFFER_NAME)
            .price(DEFAULT_PRICE)
            .coverageDetails(DEFAULT_COVERAGE_DETAILS)
            .termsAndConditions(DEFAULT_TERMS_AND_CONDITIONS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceOffer createUpdatedEntity() {
        return new InsuranceOffer()
            .offerId(UPDATED_OFFER_ID)
            .offerName(UPDATED_OFFER_NAME)
            .price(UPDATED_PRICE)
            .coverageDetails(UPDATED_COVERAGE_DETAILS)
            .termsAndConditions(UPDATED_TERMS_AND_CONDITIONS);
    }

    @BeforeEach
    public void initTest() {
        insuranceOffer = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInsuranceOffer != null) {
            insuranceOfferRepository.delete(insertedInsuranceOffer);
            insertedInsuranceOffer = null;
        }
    }

    @Test
    @Transactional
    void createInsuranceOffer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsuranceOffer
        var returnedInsuranceOffer = om.readValue(
            restInsuranceOfferMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuranceOffer))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsuranceOffer.class
        );

        // Validate the InsuranceOffer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsuranceOfferUpdatableFieldsEquals(returnedInsuranceOffer, getPersistedInsuranceOffer(returnedInsuranceOffer));

        insertedInsuranceOffer = returnedInsuranceOffer;
    }

    @Test
    @Transactional
    void createInsuranceOfferWithExistingId() throws Exception {
        // Create the InsuranceOffer with an existing ID
        insuranceOffer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceOfferMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuranceOffer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsuranceOffers() throws Exception {
        // Initialize the database
        insertedInsuranceOffer = insuranceOfferRepository.saveAndFlush(insuranceOffer);

        // Get all the insuranceOfferList
        restInsuranceOfferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceOffer.getId().intValue())))
            .andExpect(jsonPath("$.[*].offerId").value(hasItem(DEFAULT_OFFER_ID)))
            .andExpect(jsonPath("$.[*].offerName").value(hasItem(DEFAULT_OFFER_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].coverageDetails").value(hasItem(DEFAULT_COVERAGE_DETAILS)))
            .andExpect(jsonPath("$.[*].termsAndConditions").value(hasItem(DEFAULT_TERMS_AND_CONDITIONS)));
    }

    @Test
    @Transactional
    void getInsuranceOffer() throws Exception {
        // Initialize the database
        insertedInsuranceOffer = insuranceOfferRepository.saveAndFlush(insuranceOffer);

        // Get the insuranceOffer
        restInsuranceOfferMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceOffer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceOffer.getId().intValue()))
            .andExpect(jsonPath("$.offerId").value(DEFAULT_OFFER_ID))
            .andExpect(jsonPath("$.offerName").value(DEFAULT_OFFER_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.coverageDetails").value(DEFAULT_COVERAGE_DETAILS))
            .andExpect(jsonPath("$.termsAndConditions").value(DEFAULT_TERMS_AND_CONDITIONS));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceOffer() throws Exception {
        // Get the insuranceOffer
        restInsuranceOfferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceOffer() throws Exception {
        // Initialize the database
        insertedInsuranceOffer = insuranceOfferRepository.saveAndFlush(insuranceOffer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceOffer
        InsuranceOffer updatedInsuranceOffer = insuranceOfferRepository.findById(insuranceOffer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInsuranceOffer are not directly saved in db
        em.detach(updatedInsuranceOffer);
        updatedInsuranceOffer
            .offerId(UPDATED_OFFER_ID)
            .offerName(UPDATED_OFFER_NAME)
            .price(UPDATED_PRICE)
            .coverageDetails(UPDATED_COVERAGE_DETAILS)
            .termsAndConditions(UPDATED_TERMS_AND_CONDITIONS);

        restInsuranceOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsuranceOffer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsuranceOffer))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsuranceOfferToMatchAllProperties(updatedInsuranceOffer);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceOffer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceOffer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceOffer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceOffer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceOffer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceOffer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceOfferMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuranceOffer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceOfferWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceOffer = insuranceOfferRepository.saveAndFlush(insuranceOffer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceOffer using partial update
        InsuranceOffer partialUpdatedInsuranceOffer = new InsuranceOffer();
        partialUpdatedInsuranceOffer.setId(insuranceOffer.getId());

        restInsuranceOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceOffer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceOffer))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceOffer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceOfferUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsuranceOffer, insuranceOffer),
            getPersistedInsuranceOffer(insuranceOffer)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsuranceOfferWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceOffer = insuranceOfferRepository.saveAndFlush(insuranceOffer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceOffer using partial update
        InsuranceOffer partialUpdatedInsuranceOffer = new InsuranceOffer();
        partialUpdatedInsuranceOffer.setId(insuranceOffer.getId());

        partialUpdatedInsuranceOffer
            .offerId(UPDATED_OFFER_ID)
            .offerName(UPDATED_OFFER_NAME)
            .price(UPDATED_PRICE)
            .coverageDetails(UPDATED_COVERAGE_DETAILS)
            .termsAndConditions(UPDATED_TERMS_AND_CONDITIONS);

        restInsuranceOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceOffer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceOffer))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceOffer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceOfferUpdatableFieldsEquals(partialUpdatedInsuranceOffer, getPersistedInsuranceOffer(partialUpdatedInsuranceOffer));
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceOffer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceOffer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceOffer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceOffer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceOffer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceOffer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceOfferMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(insuranceOffer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceOffer() throws Exception {
        // Initialize the database
        insertedInsuranceOffer = insuranceOfferRepository.saveAndFlush(insuranceOffer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insuranceOffer
        restInsuranceOfferMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuranceOffer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insuranceOfferRepository.count();
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

    protected InsuranceOffer getPersistedInsuranceOffer(InsuranceOffer insuranceOffer) {
        return insuranceOfferRepository.findById(insuranceOffer.getId()).orElseThrow();
    }

    protected void assertPersistedInsuranceOfferToMatchAllProperties(InsuranceOffer expectedInsuranceOffer) {
        assertInsuranceOfferAllPropertiesEquals(expectedInsuranceOffer, getPersistedInsuranceOffer(expectedInsuranceOffer));
    }

    protected void assertPersistedInsuranceOfferToMatchUpdatableProperties(InsuranceOffer expectedInsuranceOffer) {
        assertInsuranceOfferAllUpdatablePropertiesEquals(expectedInsuranceOffer, getPersistedInsuranceOffer(expectedInsuranceOffer));
    }
}
