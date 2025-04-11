package com.insurance.app.repository;

import com.insurance.app.domain.VehicleUsage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleUsage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleUsageRepository extends JpaRepository<VehicleUsage, Long> {}
