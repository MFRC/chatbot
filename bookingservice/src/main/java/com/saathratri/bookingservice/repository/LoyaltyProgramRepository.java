package com.saathratri.bookingservice.repository;

import com.saathratri.bookingservice.domain.LoyaltyProgram;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LoyaltyProgram entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoyaltyProgramRepository extends JpaRepository<LoyaltyProgram, UUID> {}
