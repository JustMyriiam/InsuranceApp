package com.insurance.app.repository;

import com.insurance.app.domain.BlacklistedCar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BlacklistedCar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlacklistedCarRepository extends JpaRepository<BlacklistedCar, Long> {}
