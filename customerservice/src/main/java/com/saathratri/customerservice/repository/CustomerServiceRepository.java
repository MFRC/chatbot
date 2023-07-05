package com.saathratri.customerservice.repository;

import com.saathratri.customerservice.domain.CustomerService;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerServiceRepository extends JpaRepository<CustomerService, UUID>, JpaSpecificationExecutor<CustomerService> {}
