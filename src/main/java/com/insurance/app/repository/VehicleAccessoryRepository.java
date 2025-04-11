package com.insurance.app.repository;

import com.insurance.app.domain.VehicleAccessory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleAccessory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleAccessoryRepository extends JpaRepository<VehicleAccessory, Long> {}
