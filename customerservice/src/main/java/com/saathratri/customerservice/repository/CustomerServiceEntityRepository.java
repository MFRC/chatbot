package com.saathratri.customerservice.repository;

import com.saathratri.customerservice.domain.CustomerServiceEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerServiceEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerServiceEntityRepository
    extends JpaRepository<CustomerServiceEntity, UUID>, JpaSpecificationExecutor<CustomerServiceEntity> {}
