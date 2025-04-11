package com.insurance.app.repository;

import com.insurance.app.domain.LocationRisk;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LocationRisk entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRiskRepository extends JpaRepository<LocationRisk, Long> {}
