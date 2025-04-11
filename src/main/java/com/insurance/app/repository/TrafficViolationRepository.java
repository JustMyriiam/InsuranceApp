package com.insurance.app.repository;

import com.insurance.app.domain.TrafficViolation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrafficViolation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrafficViolationRepository extends JpaRepository<TrafficViolation, Long> {}
