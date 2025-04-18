package com.insurance.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class BurntStolenIncidentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBurntStolenIncidentAllPropertiesEquals(BurntStolenIncident expected, BurntStolenIncident actual) {
        assertBurntStolenIncidentAutoGeneratedPropertiesEquals(expected, actual);
        assertBurntStolenIncidentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBurntStolenIncidentAllUpdatablePropertiesEquals(BurntStolenIncident expected, BurntStolenIncident actual) {
        assertBurntStolenIncidentUpdatableFieldsEquals(expected, actual);
        assertBurntStolenIncidentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBurntStolenIncidentAutoGeneratedPropertiesEquals(BurntStolenIncident expected, BurntStolenIncident actual) {
        assertThat(actual)
            .as("Verify BurntStolenIncident auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBurntStolenIncidentUpdatableFieldsEquals(BurntStolenIncident expected, BurntStolenIncident actual) {
        assertThat(actual)
            .as("Verify BurntStolenIncident relevant properties")
            .satisfies(a -> assertThat(a.getIncidentId()).as("check incidentId").isEqualTo(expected.getIncidentId()))
            .satisfies(a -> assertThat(a.getIncidentDate()).as("check incidentDate").isEqualTo(expected.getIncidentDate()))
            .satisfies(a -> assertThat(a.getType()).as("check type").isEqualTo(expected.getType()))
            .satisfies(a -> assertThat(a.getDescription()).as("check description").isEqualTo(expected.getDescription()))
            .satisfies(a -> assertThat(a.getEstimatedLoss()).as("check estimatedLoss").isEqualTo(expected.getEstimatedLoss()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBurntStolenIncidentUpdatableRelationshipsEquals(BurntStolenIncident expected, BurntStolenIncident actual) {
        assertThat(actual)
            .as("Verify BurntStolenIncident relationships")
            .satisfies(a -> assertThat(a.getContract()).as("check contract").isEqualTo(expected.getContract()));
    }
}
