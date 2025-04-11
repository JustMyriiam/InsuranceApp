package com.insurance.app.repository;

import com.insurance.app.domain.InsuranceOffer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsuranceOffer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceOfferRepository extends JpaRepository<InsuranceOffer, Long> {}
