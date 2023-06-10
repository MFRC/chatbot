package com.saathratri.customerservice.repository;

import com.saathratri.customerservice.domain.End;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the End entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EndRepository extends JpaRepository<End, UUID>, JpaSpecificationExecutor<End> {}
