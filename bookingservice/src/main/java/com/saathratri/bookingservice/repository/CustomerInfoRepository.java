package com.saathratri.bookingservice.repository;

import com.saathratri.bookingservice.domain.CustomerInfo;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, UUID> {}
