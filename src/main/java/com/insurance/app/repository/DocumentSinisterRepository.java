package com.insurance.app.repository;

import com.insurance.app.domain.DocumentSinister;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentSinister entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentSinisterRepository extends JpaRepository<DocumentSinister, Long> {}
