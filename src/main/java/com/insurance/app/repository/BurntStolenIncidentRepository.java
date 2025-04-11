package com.insurance.app.repository;

import com.insurance.app.domain.BurntStolenIncident;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BurntStolenIncident entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BurntStolenIncidentRepository extends JpaRepository<BurntStolenIncident, Long> {}
