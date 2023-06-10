package com.saathratri.bookingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.bookingservice.IntegrationTest;
import com.saathratri.bookingservice.domain.Reservation;
import com.saathratri.bookingservice.repository.ReservationRepository;
import com.saathratri.bookingservice.service.dto.ReservationDTO;
import com.saathratri.bookingservice.service.mapper.ReservationMapper;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReservationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReservationResourceIT {

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_RATE_PLAN = "AAAAAAAAAA";
    private static final String UPDATED_RATE_PLAN = "BBBBBBBBBB";

    private static final Long DEFAULT_ARRIVAL_DATE = 1L;
    private static final Long UPDATED_ARRIVAL_DATE = 2L;

    private static final Long DEFAULT_DEPARTURE_DATE = 1L;
    private static final Long UPDATED_DEPARTURE_DATE = 2L;

    private static final Long DEFAULT_CHECK_IN_DATE_TIME = 1L;
    private static final Long UPDATED_CHECK_IN_DATE_TIME = 2L;

    private static final Long DEFAULT_CHECK_OUT_DATE_TIME = 1L;
    private static final Long UPDATED_CHECK_OUT_DATE_TIME = 2L;

    private static final String DEFAULT_ROOM_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ROOM_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_ADULTS = 1;
    private static final Integer UPDATED_ADULTS = 2;

    private static final Integer DEFAULT_CHILDREN = 1;
    private static final Integer UPDATED_CHILDREN = 2;

    private static final Boolean DEFAULT_CRIB = false;
    private static final Boolean UPDATED_CRIB = true;

    private static final Boolean DEFAULT_ROLLAWAY = false;
    private static final Boolean UPDATED_ROLLAWAY = true;

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reservations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservationMockMvc;

    private Reservation reservation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .status(DEFAULT_STATUS)
            .ratePlan(DEFAULT_RATE_PLAN)
            .arrivalDate(DEFAULT_ARRIVAL_DATE)
            .departureDate(DEFAULT_DEPARTURE_DATE)
            .checkInDateTime(DEFAULT_CHECK_IN_DATE_TIME)
            .checkOutDateTime(DEFAULT_CHECK_OUT_DATE_TIME)
            .roomType(DEFAULT_ROOM_TYPE)
            .roomNumber(DEFAULT_ROOM_NUMBER)
            .adults(DEFAULT_ADULTS)
            .children(DEFAULT_CHILDREN)
            .crib(DEFAULT_CRIB)
            .rollaway(DEFAULT_ROLLAWAY)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL);
        return reservation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createUpdatedEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .status(UPDATED_STATUS)
            .ratePlan(UPDATED_RATE_PLAN)
            .arrivalDate(UPDATED_ARRIVAL_DATE)
            .departureDate(UPDATED_DEPARTURE_DATE)
            .checkInDateTime(UPDATED_CHECK_IN_DATE_TIME)
            .checkOutDateTime(UPDATED_CHECK_OUT_DATE_TIME)
            .roomType(UPDATED_ROOM_TYPE)
            .roomNumber(UPDATED_ROOM_NUMBER)
            .adults(UPDATED_ADULTS)
            .children(UPDATED_CHILDREN)
            .crib(UPDATED_CRIB)
            .rollaway(UPDATED_ROLLAWAY)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);
        return reservation;
    }

    @BeforeEach
    public void initTest() {
        reservation = createEntity(em);
    }

    @Test
    @Transactional
    void createReservation() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();
        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);
        restReservationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate + 1);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testReservation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReservation.getRatePlan()).isEqualTo(DEFAULT_RATE_PLAN);
        assertThat(testReservation.getArrivalDate()).isEqualTo(DEFAULT_ARRIVAL_DATE);
        assertThat(testReservation.getDepartureDate()).isEqualTo(DEFAULT_DEPARTURE_DATE);
        assertThat(testReservation.getCheckInDateTime()).isEqualTo(DEFAULT_CHECK_IN_DATE_TIME);
        assertThat(testReservation.getCheckOutDateTime()).isEqualTo(DEFAULT_CHECK_OUT_DATE_TIME);
        assertThat(testReservation.getRoomType()).isEqualTo(DEFAULT_ROOM_TYPE);
        assertThat(testReservation.getRoomNumber()).isEqualTo(DEFAULT_ROOM_NUMBER);
        assertThat(testReservation.getAdults()).isEqualTo(DEFAULT_ADULTS);
        assertThat(testReservation.getChildren()).isEqualTo(DEFAULT_CHILDREN);
        assertThat(testReservation.getCrib()).isEqualTo(DEFAULT_CRIB);
        assertThat(testReservation.getRollaway()).isEqualTo(DEFAULT_ROLLAWAY);
        assertThat(testReservation.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testReservation.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testReservation.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testReservation.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createReservationWithExistingId() throws Exception {
        // Create the Reservation with an existing ID
        reservationRepository.saveAndFlush(reservation);
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReservations() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().toString())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].ratePlan").value(hasItem(DEFAULT_RATE_PLAN)))
            .andExpect(jsonPath("$.[*].arrivalDate").value(hasItem(DEFAULT_ARRIVAL_DATE.intValue())))
            .andExpect(jsonPath("$.[*].departureDate").value(hasItem(DEFAULT_DEPARTURE_DATE.intValue())))
            .andExpect(jsonPath("$.[*].checkInDateTime").value(hasItem(DEFAULT_CHECK_IN_DATE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].checkOutDateTime").value(hasItem(DEFAULT_CHECK_OUT_DATE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].roomType").value(hasItem(DEFAULT_ROOM_TYPE)))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)))
            .andExpect(jsonPath("$.[*].adults").value(hasItem(DEFAULT_ADULTS)))
            .andExpect(jsonPath("$.[*].children").value(hasItem(DEFAULT_CHILDREN)))
            .andExpect(jsonPath("$.[*].crib").value(hasItem(DEFAULT_CRIB.booleanValue())))
            .andExpect(jsonPath("$.[*].rollaway").value(hasItem(DEFAULT_ROLLAWAY.booleanValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get the reservation
        restReservationMockMvc
            .perform(get(ENTITY_API_URL_ID, reservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reservation.getId().toString()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.ratePlan").value(DEFAULT_RATE_PLAN))
            .andExpect(jsonPath("$.arrivalDate").value(DEFAULT_ARRIVAL_DATE.intValue()))
            .andExpect(jsonPath("$.departureDate").value(DEFAULT_DEPARTURE_DATE.intValue()))
            .andExpect(jsonPath("$.checkInDateTime").value(DEFAULT_CHECK_IN_DATE_TIME.intValue()))
            .andExpect(jsonPath("$.checkOutDateTime").value(DEFAULT_CHECK_OUT_DATE_TIME.intValue()))
            .andExpect(jsonPath("$.roomType").value(DEFAULT_ROOM_TYPE))
            .andExpect(jsonPath("$.roomNumber").value(DEFAULT_ROOM_NUMBER))
            .andExpect(jsonPath("$.adults").value(DEFAULT_ADULTS))
            .andExpect(jsonPath("$.children").value(DEFAULT_CHILDREN))
            .andExpect(jsonPath("$.crib").value(DEFAULT_CRIB.booleanValue()))
            .andExpect(jsonPath("$.rollaway").value(DEFAULT_ROLLAWAY.booleanValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingReservation() throws Exception {
        // Get the reservation
        restReservationMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation
        Reservation updatedReservation = reservationRepository.findById(reservation.getId()).get();
        // Disconnect from session so that the updates on updatedReservation are not directly saved in db
        em.detach(updatedReservation);
        updatedReservation
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .status(UPDATED_STATUS)
            .ratePlan(UPDATED_RATE_PLAN)
            .arrivalDate(UPDATED_ARRIVAL_DATE)
            .departureDate(UPDATED_DEPARTURE_DATE)
            .checkInDateTime(UPDATED_CHECK_IN_DATE_TIME)
            .checkOutDateTime(UPDATED_CHECK_OUT_DATE_TIME)
            .roomType(UPDATED_ROOM_TYPE)
            .roomNumber(UPDATED_ROOM_NUMBER)
            .adults(UPDATED_ADULTS)
            .children(UPDATED_CHILDREN)
            .crib(UPDATED_CRIB)
            .rollaway(UPDATED_ROLLAWAY)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);
        ReservationDTO reservationDTO = reservationMapper.toDto(updatedReservation);

        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testReservation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReservation.getRatePlan()).isEqualTo(UPDATED_RATE_PLAN);
        assertThat(testReservation.getArrivalDate()).isEqualTo(UPDATED_ARRIVAL_DATE);
        assertThat(testReservation.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
        assertThat(testReservation.getCheckInDateTime()).isEqualTo(UPDATED_CHECK_IN_DATE_TIME);
        assertThat(testReservation.getCheckOutDateTime()).isEqualTo(UPDATED_CHECK_OUT_DATE_TIME);
        assertThat(testReservation.getRoomType()).isEqualTo(UPDATED_ROOM_TYPE);
        assertThat(testReservation.getRoomNumber()).isEqualTo(UPDATED_ROOM_NUMBER);
        assertThat(testReservation.getAdults()).isEqualTo(UPDATED_ADULTS);
        assertThat(testReservation.getChildren()).isEqualTo(UPDATED_CHILDREN);
        assertThat(testReservation.getCrib()).isEqualTo(UPDATED_CRIB);
        assertThat(testReservation.getRollaway()).isEqualTo(UPDATED_ROLLAWAY);
        assertThat(testReservation.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testReservation.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testReservation.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testReservation.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservationWithPatch() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation using partial update
        Reservation partialUpdatedReservation = new Reservation();
        partialUpdatedReservation.setId(reservation.getId());

        partialUpdatedReservation
            .ratePlan(UPDATED_RATE_PLAN)
            .departureDate(UPDATED_DEPARTURE_DATE)
            .checkInDateTime(UPDATED_CHECK_IN_DATE_TIME)
            .checkOutDateTime(UPDATED_CHECK_OUT_DATE_TIME)
            .roomType(UPDATED_ROOM_TYPE)
            .roomNumber(UPDATED_ROOM_NUMBER)
            .children(UPDATED_CHILDREN)
            .lastName(UPDATED_LAST_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);

        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservation))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testReservation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReservation.getRatePlan()).isEqualTo(UPDATED_RATE_PLAN);
        assertThat(testReservation.getArrivalDate()).isEqualTo(DEFAULT_ARRIVAL_DATE);
        assertThat(testReservation.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
        assertThat(testReservation.getCheckInDateTime()).isEqualTo(UPDATED_CHECK_IN_DATE_TIME);
        assertThat(testReservation.getCheckOutDateTime()).isEqualTo(UPDATED_CHECK_OUT_DATE_TIME);
        assertThat(testReservation.getRoomType()).isEqualTo(UPDATED_ROOM_TYPE);
        assertThat(testReservation.getRoomNumber()).isEqualTo(UPDATED_ROOM_NUMBER);
        assertThat(testReservation.getAdults()).isEqualTo(DEFAULT_ADULTS);
        assertThat(testReservation.getChildren()).isEqualTo(UPDATED_CHILDREN);
        assertThat(testReservation.getCrib()).isEqualTo(DEFAULT_CRIB);
        assertThat(testReservation.getRollaway()).isEqualTo(DEFAULT_ROLLAWAY);
        assertThat(testReservation.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testReservation.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testReservation.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testReservation.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateReservationWithPatch() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation using partial update
        Reservation partialUpdatedReservation = new Reservation();
        partialUpdatedReservation.setId(reservation.getId());

        partialUpdatedReservation
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .status(UPDATED_STATUS)
            .ratePlan(UPDATED_RATE_PLAN)
            .arrivalDate(UPDATED_ARRIVAL_DATE)
            .departureDate(UPDATED_DEPARTURE_DATE)
            .checkInDateTime(UPDATED_CHECK_IN_DATE_TIME)
            .checkOutDateTime(UPDATED_CHECK_OUT_DATE_TIME)
            .roomType(UPDATED_ROOM_TYPE)
            .roomNumber(UPDATED_ROOM_NUMBER)
            .adults(UPDATED_ADULTS)
            .children(UPDATED_CHILDREN)
            .crib(UPDATED_CRIB)
            .rollaway(UPDATED_ROLLAWAY)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);

        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservation))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testReservation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReservation.getRatePlan()).isEqualTo(UPDATED_RATE_PLAN);
        assertThat(testReservation.getArrivalDate()).isEqualTo(UPDATED_ARRIVAL_DATE);
        assertThat(testReservation.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
        assertThat(testReservation.getCheckInDateTime()).isEqualTo(UPDATED_CHECK_IN_DATE_TIME);
        assertThat(testReservation.getCheckOutDateTime()).isEqualTo(UPDATED_CHECK_OUT_DATE_TIME);
        assertThat(testReservation.getRoomType()).isEqualTo(UPDATED_ROOM_TYPE);
        assertThat(testReservation.getRoomNumber()).isEqualTo(UPDATED_ROOM_NUMBER);
        assertThat(testReservation.getAdults()).isEqualTo(UPDATED_ADULTS);
        assertThat(testReservation.getChildren()).isEqualTo(UPDATED_CHILDREN);
        assertThat(testReservation.getCrib()).isEqualTo(UPDATED_CRIB);
        assertThat(testReservation.getRollaway()).isEqualTo(UPDATED_ROLLAWAY);
        assertThat(testReservation.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testReservation.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testReservation.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testReservation.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reservationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeDelete = reservationRepository.findAll().size();

        // Delete the reservation
        restReservationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reservation.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
