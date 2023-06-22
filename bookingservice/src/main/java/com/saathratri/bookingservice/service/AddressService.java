package com.saathratri.bookingservice.service;

import com.saathratri.bookingservice.service.dto.AddressDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.saathratri.bookingservice.domain.Address}.
 */
public interface AddressService {
    /**
     * Save a address.
     *
     * @param addressDTO the entity to save.
     * @return the persisted entity.
     */
    AddressDTO save(AddressDTO addressDTO);

    /**
     * Updates a address.
     *
     * @param addressDTO the entity to update.
     * @return the persisted entity.
     */
    AddressDTO update(AddressDTO addressDTO);

    /**
     * Partially updates a address.
     *
     * @param addressDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AddressDTO> partialUpdate(AddressDTO addressDTO);

    /**
     * Get all the addresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AddressDTO> findAll(Pageable pageable);
    /**
     * Get all the AddressDTO where Customer is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AddressDTO> findAllWhereCustomerIsNull();

    /**
     * Get the "id" address.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AddressDTO> findOne(UUID id);

    /**
     * Delete the "id" address.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
