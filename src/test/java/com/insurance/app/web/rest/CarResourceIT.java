package com.insurance.app.web.rest;

import static com.insurance.app.domain.CarAsserts.*;
import static com.insurance.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.app.IntegrationTest;
import com.insurance.app.domain.Car;
import com.insurance.app.repository.CarRepository;
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
 * Integration tests for the {@link CarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarResourceIT {

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FUEL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FUEL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSMISSION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSMISSION = "BBBBBBBBBB";

    private static final Double DEFAULT_ENGINE_SIZE = 1D;
    private static final Double UPDATED_ENGINE_SIZE = 2D;

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_MILEAGE = 1;
    private static final Integer UPDATED_MILEAGE = 2;

    private static final String DEFAULT_INSURANCE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_INSURANCE_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_CAR_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CAR_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_BLACKLISTED = false;
    private static final Boolean UPDATED_IS_BLACKLISTED = true;

    private static final Double DEFAULT_PRICE_WHEN_BOUGHT = 1D;
    private static final Double UPDATED_PRICE_WHEN_BOUGHT = 2D;

    private static final Double DEFAULT_CURRENT_PRICE = 1D;
    private static final Double UPDATED_CURRENT_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarMockMvc;

    private Car car;

    private Car insertedCar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity() {
        return new Car()
            .brand(DEFAULT_BRAND)
            .model(DEFAULT_MODEL)
            .year(DEFAULT_YEAR)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER)
            .fuelType(DEFAULT_FUEL_TYPE)
            .transmission(DEFAULT_TRANSMISSION)
            .engineSize(DEFAULT_ENGINE_SIZE)
            .color(DEFAULT_COLOR)
            .mileage(DEFAULT_MILEAGE)
            .insuranceStatus(DEFAULT_INSURANCE_STATUS)
            .carType(DEFAULT_CAR_TYPE)
            .isBlacklisted(DEFAULT_IS_BLACKLISTED)
            .priceWhenBought(DEFAULT_PRICE_WHEN_BOUGHT)
            .currentPrice(DEFAULT_CURRENT_PRICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity() {
        return new Car()
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .year(UPDATED_YEAR)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .fuelType(UPDATED_FUEL_TYPE)
            .transmission(UPDATED_TRANSMISSION)
            .engineSize(UPDATED_ENGINE_SIZE)
            .color(UPDATED_COLOR)
            .mileage(UPDATED_MILEAGE)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .carType(UPDATED_CAR_TYPE)
            .isBlacklisted(UPDATED_IS_BLACKLISTED)
            .priceWhenBought(UPDATED_PRICE_WHEN_BOUGHT)
            .currentPrice(UPDATED_CURRENT_PRICE);
    }

    @BeforeEach
    public void initTest() {
        car = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCar != null) {
            carRepository.delete(insertedCar);
            insertedCar = null;
        }
    }

    @Test
    @Transactional
    void createCar() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Car
        var returnedCar = om.readValue(
            restCarMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Car.class
        );

        // Validate the Car in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCarUpdatableFieldsEquals(returnedCar, getPersistedCar(returnedCar));

        insertedCar = returnedCar;
    }

    @Test
    @Transactional
    void createCarWithExistingId() throws Exception {
        // Create the Car with an existing ID
        car.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCars() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].fuelType").value(hasItem(DEFAULT_FUEL_TYPE)))
            .andExpect(jsonPath("$.[*].transmission").value(hasItem(DEFAULT_TRANSMISSION)))
            .andExpect(jsonPath("$.[*].engineSize").value(hasItem(DEFAULT_ENGINE_SIZE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].mileage").value(hasItem(DEFAULT_MILEAGE)))
            .andExpect(jsonPath("$.[*].insuranceStatus").value(hasItem(DEFAULT_INSURANCE_STATUS)))
            .andExpect(jsonPath("$.[*].carType").value(hasItem(DEFAULT_CAR_TYPE)))
            .andExpect(jsonPath("$.[*].isBlacklisted").value(hasItem(DEFAULT_IS_BLACKLISTED)))
            .andExpect(jsonPath("$.[*].priceWhenBought").value(hasItem(DEFAULT_PRICE_WHEN_BOUGHT)))
            .andExpect(jsonPath("$.[*].currentPrice").value(hasItem(DEFAULT_CURRENT_PRICE)));
    }

    @Test
    @Transactional
    void getCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc
            .perform(get(ENTITY_API_URL_ID, car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER))
            .andExpect(jsonPath("$.fuelType").value(DEFAULT_FUEL_TYPE))
            .andExpect(jsonPath("$.transmission").value(DEFAULT_TRANSMISSION))
            .andExpect(jsonPath("$.engineSize").value(DEFAULT_ENGINE_SIZE))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.mileage").value(DEFAULT_MILEAGE))
            .andExpect(jsonPath("$.insuranceStatus").value(DEFAULT_INSURANCE_STATUS))
            .andExpect(jsonPath("$.carType").value(DEFAULT_CAR_TYPE))
            .andExpect(jsonPath("$.isBlacklisted").value(DEFAULT_IS_BLACKLISTED))
            .andExpect(jsonPath("$.priceWhenBought").value(DEFAULT_PRICE_WHEN_BOUGHT))
            .andExpect(jsonPath("$.currentPrice").value(DEFAULT_CURRENT_PRICE));
    }

    @Test
    @Transactional
    void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .year(UPDATED_YEAR)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .fuelType(UPDATED_FUEL_TYPE)
            .transmission(UPDATED_TRANSMISSION)
            .engineSize(UPDATED_ENGINE_SIZE)
            .color(UPDATED_COLOR)
            .mileage(UPDATED_MILEAGE)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .carType(UPDATED_CAR_TYPE)
            .isBlacklisted(UPDATED_IS_BLACKLISTED)
            .priceWhenBought(UPDATED_PRICE_WHEN_BOUGHT)
            .currentPrice(UPDATED_CURRENT_PRICE);

        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCar.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarToMatchAllProperties(updatedCar);
    }

    @Test
    @Transactional
    void putNonExistingCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, car.getId()).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarWithPatch() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar
            .model(UPDATED_MODEL)
            .fuelType(UPDATED_FUEL_TYPE)
            .transmission(UPDATED_TRANSMISSION)
            .color(UPDATED_COLOR)
            .mileage(UPDATED_MILEAGE)
            .isBlacklisted(UPDATED_IS_BLACKLISTED)
            .priceWhenBought(UPDATED_PRICE_WHEN_BOUGHT);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCar, car), getPersistedCar(car));
    }

    @Test
    @Transactional
    void fullUpdateCarWithPatch() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .year(UPDATED_YEAR)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .fuelType(UPDATED_FUEL_TYPE)
            .transmission(UPDATED_TRANSMISSION)
            .engineSize(UPDATED_ENGINE_SIZE)
            .color(UPDATED_COLOR)
            .mileage(UPDATED_MILEAGE)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .carType(UPDATED_CAR_TYPE)
            .isBlacklisted(UPDATED_IS_BLACKLISTED)
            .priceWhenBought(UPDATED_PRICE_WHEN_BOUGHT)
            .currentPrice(UPDATED_CURRENT_PRICE);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarUpdatableFieldsEquals(partialUpdatedCar, getPersistedCar(partialUpdatedCar));
    }

    @Test
    @Transactional
    void patchNonExistingCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, car.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(car)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the car
        restCarMockMvc
            .perform(delete(ENTITY_API_URL_ID, car.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carRepository.count();
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

    protected Car getPersistedCar(Car car) {
        return carRepository.findById(car.getId()).orElseThrow();
    }

    protected void assertPersistedCarToMatchAllProperties(Car expectedCar) {
        assertCarAllPropertiesEquals(expectedCar, getPersistedCar(expectedCar));
    }

    protected void assertPersistedCarToMatchUpdatableProperties(Car expectedCar) {
        assertCarAllUpdatablePropertiesEquals(expectedCar, getPersistedCar(expectedCar));
    }
}
