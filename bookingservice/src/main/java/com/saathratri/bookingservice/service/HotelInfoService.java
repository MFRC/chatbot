package com.saathratri.bookingservice.service;

import com.saathratri.bookingservice.service.dto.HotelInfoDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.saathratri.bookingservice.domain.HotelInfo}.
 */
public interface HotelInfoService {
    /**
     * Save a hotelInfo.
     *
     * @param hotelInfoDTO the entity to save.
     * @return the persisted entity.
     */
    HotelInfoDTO save(HotelInfoDTO hotelInfoDTO);

    /**
     * Updates a hotelInfo.
     *
     * @param hotelInfoDTO the entity to update.
     * @return the persisted entity.
     */
    HotelInfoDTO update(HotelInfoDTO hotelInfoDTO);

    /**
     * Partially updates a hotelInfo.
     *
     * @param hotelInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HotelInfoDTO> partialUpdate(HotelInfoDTO hotelInfoDTO);

    /**
     * Get all the hotelInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HotelInfoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" hotelInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HotelInfoDTO> findOne(UUID id);

    /**
     * Delete the "id" hotelInfo.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
