package com.insurance.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ParkingAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertParkingAllPropertiesEquals(Parking expected, Parking actual) {
        assertParkingAutoGeneratedPropertiesEquals(expected, actual);
        assertParkingAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertParkingAllUpdatablePropertiesEquals(Parking expected, Parking actual) {
        assertParkingUpdatableFieldsEquals(expected, actual);
        assertParkingUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertParkingAutoGeneratedPropertiesEquals(Parking expected, Parking actual) {
        assertThat(actual)
            .as("Verify Parking auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertParkingUpdatableFieldsEquals(Parking expected, Parking actual) {
        assertThat(actual)
            .as("Verify Parking relevant properties")
            .satisfies(a -> assertThat(a.getParkingId()).as("check parkingId").isEqualTo(expected.getParkingId()))
            .satisfies(a -> assertThat(a.getLocation()).as("check location").isEqualTo(expected.getLocation()))
            .satisfies(a -> assertThat(a.getIsSecured()).as("check isSecured").isEqualTo(expected.getIsSecured()))
            .satisfies(a -> assertThat(a.getCapacity()).as("check capacity").isEqualTo(expected.getCapacity()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertParkingUpdatableRelationshipsEquals(Parking expected, Parking actual) {
        assertThat(actual)
            .as("Verify Parking relationships")
            .satisfies(a -> assertThat(a.getContract()).as("check contract").isEqualTo(expected.getContract()));
    }
}
