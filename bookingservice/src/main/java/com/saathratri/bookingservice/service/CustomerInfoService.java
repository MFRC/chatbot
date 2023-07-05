package com.saathratri.bookingservice.service;

import com.saathratri.bookingservice.service.dto.CustomerInfoDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.saathratri.bookingservice.domain.CustomerInfo}.
 */
public interface CustomerInfoService {
    /**
     * Save a customerInfo.
     *
     * @param customerInfoDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerInfoDTO save(CustomerInfoDTO customerInfoDTO);

    /**
     * Updates a customerInfo.
     *
     * @param customerInfoDTO the entity to update.
     * @return the persisted entity.
     */
    CustomerInfoDTO update(CustomerInfoDTO customerInfoDTO);

    /**
     * Partially updates a customerInfo.
     *
     * @param customerInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerInfoDTO> partialUpdate(CustomerInfoDTO customerInfoDTO);

    /**
     * Get all the customerInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomerInfoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" customerInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerInfoDTO> findOne(UUID id);

    /**
     * Delete the "id" customerInfo.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
