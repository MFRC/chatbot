package com.saathratri.bookingservice.service;

import com.saathratri.bookingservice.service.dto.ReservationDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.saathratri.bookingservice.domain.Reservation}.
 */
public interface ReservationService {
    /**
     * Save a reservation.
     *
     * @param reservationDTO the entity to save.
     * @return the persisted entity.
     */
    ReservationDTO save(ReservationDTO reservationDTO);

    /**
     * Updates a reservation.
     *
     * @param reservationDTO the entity to update.
     * @return the persisted entity.
     */
    ReservationDTO update(ReservationDTO reservationDTO);

    /**
     * Partially updates a reservation.
     *
     * @param reservationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReservationDTO> partialUpdate(ReservationDTO reservationDTO);

    /**
     * Get all the reservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReservationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" reservation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReservationDTO> findOne(UUID id);

    /**
     * Delete the "id" reservation.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
