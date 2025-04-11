package com.insurance.app.repository;

import com.insurance.app.domain.AccidentHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccidentHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccidentHistoryRepository extends JpaRepository<AccidentHistory, Long> {}
