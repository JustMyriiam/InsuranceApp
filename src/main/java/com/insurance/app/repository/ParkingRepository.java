package com.insurance.app.repository;

import com.insurance.app.domain.Parking;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Parking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {}
