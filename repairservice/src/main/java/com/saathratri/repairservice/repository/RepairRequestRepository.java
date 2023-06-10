package com.saathratri.repairservice.repository;

import com.saathratri.repairservice.domain.RepairRequest;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RepairRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, UUID>, JpaSpecificationExecutor<RepairRequest> {}
