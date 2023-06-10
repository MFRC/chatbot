package com.saathratri.bookingservice.repository;

import com.saathratri.bookingservice.domain.HotelInfo;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HotelInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HotelInfoRepository extends JpaRepository<HotelInfo, UUID> {}
