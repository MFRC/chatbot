package com.saathratri.bookingservice.service;

import com.saathratri.bookingservice.service.dto.LoyaltyProgramDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.saathratri.bookingservice.domain.LoyaltyProgram}.
 */
public interface LoyaltyProgramService {
    /**
     * Save a loyaltyProgram.
     *
     * @param loyaltyProgramDTO the entity to save.
     * @return the persisted entity.
     */
    LoyaltyProgramDTO save(LoyaltyProgramDTO loyaltyProgramDTO);

    /**
     * Updates a loyaltyProgram.
     *
     * @param loyaltyProgramDTO the entity to update.
     * @return the persisted entity.
     */
    LoyaltyProgramDTO update(LoyaltyProgramDTO loyaltyProgramDTO);

    /**
     * Partially updates a loyaltyProgram.
     *
     * @param loyaltyProgramDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoyaltyProgramDTO> partialUpdate(LoyaltyProgramDTO loyaltyProgramDTO);

    /**
     * Get all the loyaltyPrograms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoyaltyProgramDTO> findAll(Pageable pageable);
    /**
     * Get all the LoyaltyProgramDTO where Hotel is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<LoyaltyProgramDTO> findAllWhereHotelIsNull();

    /**
     * Get the "id" loyaltyProgram.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoyaltyProgramDTO> findOne(UUID id);

    /**
     * Delete the "id" loyaltyProgram.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
