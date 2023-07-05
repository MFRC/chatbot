package com.saathratri.customerservice.repository;

import com.saathratri.customerservice.domain.FAQs;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FAQs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FAQsRepository extends JpaRepository<FAQs, UUID>, JpaSpecificationExecutor<FAQs> {}
