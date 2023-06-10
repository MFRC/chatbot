package com.saathratri.customerservice.repository;

import com.saathratri.customerservice.domain.CustomerServiceUser;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerServiceUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerServiceUserRepository
    extends JpaRepository<CustomerServiceUser, UUID>, JpaSpecificationExecutor<CustomerServiceUser> {}
