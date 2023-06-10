package com.saathratri.repairservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.repairservice.IntegrationTest;
import com.saathratri.repairservice.domain.Booking;
import com.saathratri.repairservice.domain.Customer;
import com.saathratri.repairservice.domain.Payment;
import com.saathratri.repairservice.repository.BookingRepository;
import com.saathratri.repairservice.service.criteria.BookingCriteria;
import com.saathratri.repairservice.service.dto.BookingDTO;
import com.saathratri.repairservice.service.mapper.BookingMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link BookingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookingResourceIT {

    private static final UUID DEFAULT_BOOKING_ID = UUID.randomUUID();
    private static final UUID UPDATED_BOOKING_ID = UUID.randomUUID();

    private static final UUID DEFAULT_CUSTOMER_ID = UUID.randomUUID();
    private static final UUID UPDATED_CUSTOMER_ID = UUID.randomUUID();

    private static final UUID DEFAULT_ROOM_ID = UUID.randomUUID();
    private static final UUID UPDATED_ROOM_ID = UUID.randomUUID();

    private static final LocalDate DEFAULT_CHECK_IN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CHECK_IN_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CHECK_IN_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CHECK_OUT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CHECK_OUT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CHECK_OUT_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;
    private static final Double SMALLER_TOTAL_PRICE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/bookings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingMockMvc;

    private Booking booking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createEntity(EntityManager em) {
        Booking booking = new Booking()
            .bookingId(DEFAULT_BOOKING_ID)
            .customerId(DEFAULT_CUSTOMER_ID)
            .roomId(DEFAULT_ROOM_ID)
            .checkInDate(DEFAULT_CHECK_IN_DATE)
            .checkOutDate(DEFAULT_CHECK_OUT_DATE)
            .totalPrice(DEFAULT_TOTAL_PRICE);
        return booking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createUpdatedEntity(EntityManager em) {
        Booking booking = new Booking()
            .bookingId(UPDATED_BOOKING_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .roomId(UPDATED_ROOM_ID)
            .checkInDate(UPDATED_CHECK_IN_DATE)
            .checkOutDate(UPDATED_CHECK_OUT_DATE)
            .totalPrice(UPDATED_TOTAL_PRICE);
        return booking;
    }

    @BeforeEach
    public void initTest() {
        booking = createEntity(em);
    }

    @Test
    @Transactional
    void createBooking() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();
        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);
        restBookingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate + 1);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getBookingId()).isEqualTo(DEFAULT_BOOKING_ID);
        assertThat(testBooking.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testBooking.getRoomId()).isEqualTo(DEFAULT_ROOM_ID);
        assertThat(testBooking.getCheckInDate()).isEqualTo(DEFAULT_CHECK_IN_DATE);
        assertThat(testBooking.getCheckOutDate()).isEqualTo(DEFAULT_CHECK_OUT_DATE);
        assertThat(testBooking.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void createBookingWithExistingId() throws Exception {
        // Create the Booking with an existing ID
        bookingRepository.saveAndFlush(booking);
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBookings() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().toString())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.toString())))
            .andExpect(jsonPath("$.[*].roomId").value(hasItem(DEFAULT_ROOM_ID.toString())))
            .andExpect(jsonPath("$.[*].checkInDate").value(hasItem(DEFAULT_CHECK_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkOutDate").value(hasItem(DEFAULT_CHECK_OUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc
            .perform(get(ENTITY_API_URL_ID, booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booking.getId().toString()))
            .andExpect(jsonPath("$.bookingId").value(DEFAULT_BOOKING_ID.toString()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.toString()))
            .andExpect(jsonPath("$.roomId").value(DEFAULT_ROOM_ID.toString()))
            .andExpect(jsonPath("$.checkInDate").value(DEFAULT_CHECK_IN_DATE.toString()))
            .andExpect(jsonPath("$.checkOutDate").value(DEFAULT_CHECK_OUT_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getBookingsByIdFiltering() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        UUID id = booking.getId();

        defaultBookingShouldBeFound("id.equals=" + id);
        defaultBookingShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllBookingsByBookingIdIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingId equals to DEFAULT_BOOKING_ID
        defaultBookingShouldBeFound("bookingId.equals=" + DEFAULT_BOOKING_ID);

        // Get all the bookingList where bookingId equals to UPDATED_BOOKING_ID
        defaultBookingShouldNotBeFound("bookingId.equals=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllBookingsByBookingIdIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingId in DEFAULT_BOOKING_ID or UPDATED_BOOKING_ID
        defaultBookingShouldBeFound("bookingId.in=" + DEFAULT_BOOKING_ID + "," + UPDATED_BOOKING_ID);

        // Get all the bookingList where bookingId equals to UPDATED_BOOKING_ID
        defaultBookingShouldNotBeFound("bookingId.in=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllBookingsByBookingIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingId is not null
        defaultBookingShouldBeFound("bookingId.specified=true");

        // Get all the bookingList where bookingId is null
        defaultBookingShouldNotBeFound("bookingId.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultBookingShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the bookingList where customerId equals to UPDATED_CUSTOMER_ID
        defaultBookingShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultBookingShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the bookingList where customerId equals to UPDATED_CUSTOMER_ID
        defaultBookingShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where customerId is not null
        defaultBookingShouldBeFound("customerId.specified=true");

        // Get all the bookingList where customerId is null
        defaultBookingShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByRoomIdIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where roomId equals to DEFAULT_ROOM_ID
        defaultBookingShouldBeFound("roomId.equals=" + DEFAULT_ROOM_ID);

        // Get all the bookingList where roomId equals to UPDATED_ROOM_ID
        defaultBookingShouldNotBeFound("roomId.equals=" + UPDATED_ROOM_ID);
    }

    @Test
    @Transactional
    void getAllBookingsByRoomIdIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where roomId in DEFAULT_ROOM_ID or UPDATED_ROOM_ID
        defaultBookingShouldBeFound("roomId.in=" + DEFAULT_ROOM_ID + "," + UPDATED_ROOM_ID);

        // Get all the bookingList where roomId equals to UPDATED_ROOM_ID
        defaultBookingShouldNotBeFound("roomId.in=" + UPDATED_ROOM_ID);
    }

    @Test
    @Transactional
    void getAllBookingsByRoomIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where roomId is not null
        defaultBookingShouldBeFound("roomId.specified=true");

        // Get all the bookingList where roomId is null
        defaultBookingShouldNotBeFound("roomId.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCheckInDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkInDate equals to DEFAULT_CHECK_IN_DATE
        defaultBookingShouldBeFound("checkInDate.equals=" + DEFAULT_CHECK_IN_DATE);

        // Get all the bookingList where checkInDate equals to UPDATED_CHECK_IN_DATE
        defaultBookingShouldNotBeFound("checkInDate.equals=" + UPDATED_CHECK_IN_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByCheckInDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkInDate in DEFAULT_CHECK_IN_DATE or UPDATED_CHECK_IN_DATE
        defaultBookingShouldBeFound("checkInDate.in=" + DEFAULT_CHECK_IN_DATE + "," + UPDATED_CHECK_IN_DATE);

        // Get all the bookingList where checkInDate equals to UPDATED_CHECK_IN_DATE
        defaultBookingShouldNotBeFound("checkInDate.in=" + UPDATED_CHECK_IN_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByCheckInDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkInDate is not null
        defaultBookingShouldBeFound("checkInDate.specified=true");

        // Get all the bookingList where checkInDate is null
        defaultBookingShouldNotBeFound("checkInDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCheckInDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkInDate is greater than or equal to DEFAULT_CHECK_IN_DATE
        defaultBookingShouldBeFound("checkInDate.greaterThanOrEqual=" + DEFAULT_CHECK_IN_DATE);

        // Get all the bookingList where checkInDate is greater than or equal to UPDATED_CHECK_IN_DATE
        defaultBookingShouldNotBeFound("checkInDate.greaterThanOrEqual=" + UPDATED_CHECK_IN_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByCheckInDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkInDate is less than or equal to DEFAULT_CHECK_IN_DATE
        defaultBookingShouldBeFound("checkInDate.lessThanOrEqual=" + DEFAULT_CHECK_IN_DATE);

        // Get all the bookingList where checkInDate is less than or equal to SMALLER_CHECK_IN_DATE
        defaultBookingShouldNotBeFound("checkInDate.lessThanOrEqual=" + SMALLER_CHECK_IN_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByCheckInDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkInDate is less than DEFAULT_CHECK_IN_DATE
        defaultBookingShouldNotBeFound("checkInDate.lessThan=" + DEFAULT_CHECK_IN_DATE);

        // Get all the bookingList where checkInDate is less than UPDATED_CHECK_IN_DATE
        defaultBookingShouldBeFound("checkInDate.lessThan=" + UPDATED_CHECK_IN_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByCheckInDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkInDate is greater than DEFAULT_CHECK_IN_DATE
        defaultBookingShouldNotBeFound("checkInDate.greaterThan=" + DEFAULT_CHECK_IN_DATE);

        // Get all the bookingList where checkInDate is greater than SMALLER_CHECK_IN_DATE
        defaultBookingShouldBeFound("checkInDate.greaterThan=" + SMALLER_CHECK_IN_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByCheckOutDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkOutDate equals to DEFAULT_CHECK_OUT_DATE
        defaultBookingShouldBeFound("checkOutDate.equals=" + DEFAULT_CHECK_OUT_DATE);

        // Get all the bookingList where checkOutDate equals to UPDATED_CHECK_OUT_DATE
        defaultBookingShouldNotBeFound("checkOutDate.equals=" + UPDATED_CHECK_OUT_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByCheckOutDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkOutDate in DEFAULT_CHECK_OUT_DATE or UPDATED_CHECK_OUT_DATE
        defaultBookingShouldBeFound("checkOutDate.in=" + DEFAULT_CHECK_OUT_DATE + "," + UPDATED_CHECK_OUT_DATE);

        // Get all the bookingList where checkOutDate equals to UPDATED_CHECK_OUT_DATE
        defaultBookingShouldNotBeFound("checkOutDate.in=" + UPDATED_CHECK_OUT_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByCheckOutDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkOutDate is not null
        defaultBookingShouldBeFound("checkOutDate.specified=true");

        // Get all the bookingList where checkOutDate is null
        defaultBookingShouldNotBeFound("checkOutDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCheckOutDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkOutDate is greater than or equal to DEFAULT_CHECK_OUT_DATE
        defaultBookingShouldBeFound("checkOutDate.greaterThanOrEqual=" + DEFAULT_CHECK_OUT_DATE);

        // Get all the bookingList where checkOutDate is greater than or equal to UPDATED_CHECK_OUT_DATE
        defaultBookingShouldNotBeFound("checkOutDate.greaterThanOrEqual=" + UPDATED_CHECK_OUT_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByCheckOutDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkOutDate is less than or equal to DEFAULT_CHECK_OUT_DATE
        defaultBookingShouldBeFound("checkOutDate.lessThanOrEqual=" + DEFAULT_CHECK_OUT_DATE);

        // Get all the bookingList where checkOutDate is less than or equal to SMALLER_CHECK_OUT_DATE
        defaultBookingShouldNotBeFound("checkOutDate.lessThanOrEqual=" + SMALLER_CHECK_OUT_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByCheckOutDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkOutDate is less than DEFAULT_CHECK_OUT_DATE
        defaultBookingShouldNotBeFound("checkOutDate.lessThan=" + DEFAULT_CHECK_OUT_DATE);

        // Get all the bookingList where checkOutDate is less than UPDATED_CHECK_OUT_DATE
        defaultBookingShouldBeFound("checkOutDate.lessThan=" + UPDATED_CHECK_OUT_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByCheckOutDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where checkOutDate is greater than DEFAULT_CHECK_OUT_DATE
        defaultBookingShouldNotBeFound("checkOutDate.greaterThan=" + DEFAULT_CHECK_OUT_DATE);

        // Get all the bookingList where checkOutDate is greater than SMALLER_CHECK_OUT_DATE
        defaultBookingShouldBeFound("checkOutDate.greaterThan=" + SMALLER_CHECK_OUT_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultBookingShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the bookingList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultBookingShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultBookingShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the bookingList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultBookingShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalPrice is not null
        defaultBookingShouldBeFound("totalPrice.specified=true");

        // Get all the bookingList where totalPrice is null
        defaultBookingShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalPrice is greater than or equal to DEFAULT_TOTAL_PRICE
        defaultBookingShouldBeFound("totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the bookingList where totalPrice is greater than or equal to UPDATED_TOTAL_PRICE
        defaultBookingShouldNotBeFound("totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalPrice is less than or equal to DEFAULT_TOTAL_PRICE
        defaultBookingShouldBeFound("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the bookingList where totalPrice is less than or equal to SMALLER_TOTAL_PRICE
        defaultBookingShouldNotBeFound("totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalPrice is less than DEFAULT_TOTAL_PRICE
        defaultBookingShouldNotBeFound("totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the bookingList where totalPrice is less than UPDATED_TOTAL_PRICE
        defaultBookingShouldBeFound("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalPrice is greater than DEFAULT_TOTAL_PRICE
        defaultBookingShouldNotBeFound("totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the bookingList where totalPrice is greater than SMALLER_TOTAL_PRICE
        defaultBookingShouldBeFound("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllBookingsByPaymentIsEqualToSomething() throws Exception {
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            payment = PaymentResourceIT.createEntity(em);
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        em.persist(payment);
        em.flush();
        booking.setPayment(payment);
        bookingRepository.saveAndFlush(booking);
        UUID paymentId = payment.getId();

        // Get all the bookingList where payment equals to paymentId
        defaultBookingShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the bookingList where payment equals to UUID.randomUUID()
        defaultBookingShouldNotBeFound("paymentId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        booking.setCustomer(customer);
        bookingRepository.saveAndFlush(booking);
        UUID customerId = customer.getId();

        // Get all the bookingList where customer equals to customerId
        defaultBookingShouldBeFound("customerId.equals=" + customerId);

        // Get all the bookingList where customer equals to UUID.randomUUID()
        defaultBookingShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookingShouldBeFound(String filter) throws Exception {
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().toString())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.toString())))
            .andExpect(jsonPath("$.[*].roomId").value(hasItem(DEFAULT_ROOM_ID.toString())))
            .andExpect(jsonPath("$.[*].checkInDate").value(hasItem(DEFAULT_CHECK_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkOutDate").value(hasItem(DEFAULT_CHECK_OUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookingShouldNotBeFound(String filter) throws Exception {
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        // Disconnect from session so that the updates on updatedBooking are not directly saved in db
        em.detach(updatedBooking);
        updatedBooking
            .bookingId(UPDATED_BOOKING_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .roomId(UPDATED_ROOM_ID)
            .checkInDate(UPDATED_CHECK_IN_DATE)
            .checkOutDate(UPDATED_CHECK_OUT_DATE)
            .totalPrice(UPDATED_TOTAL_PRICE);
        BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testBooking.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testBooking.getRoomId()).isEqualTo(UPDATED_ROOM_ID);
        assertThat(testBooking.getCheckInDate()).isEqualTo(UPDATED_CHECK_IN_DATE);
        assertThat(testBooking.getCheckOutDate()).isEqualTo(UPDATED_CHECK_OUT_DATE);
        assertThat(testBooking.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setId(UUID.randomUUID());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setId(UUID.randomUUID());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setId(UUID.randomUUID());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingWithPatch() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking using partial update
        Booking partialUpdatedBooking = new Booking();
        partialUpdatedBooking.setId(booking.getId());

        partialUpdatedBooking.customerId(UPDATED_CUSTOMER_ID).checkOutDate(UPDATED_CHECK_OUT_DATE).totalPrice(UPDATED_TOTAL_PRICE);

        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooking.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooking))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getBookingId()).isEqualTo(DEFAULT_BOOKING_ID);
        assertThat(testBooking.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testBooking.getRoomId()).isEqualTo(DEFAULT_ROOM_ID);
        assertThat(testBooking.getCheckInDate()).isEqualTo(DEFAULT_CHECK_IN_DATE);
        assertThat(testBooking.getCheckOutDate()).isEqualTo(UPDATED_CHECK_OUT_DATE);
        assertThat(testBooking.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateBookingWithPatch() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking using partial update
        Booking partialUpdatedBooking = new Booking();
        partialUpdatedBooking.setId(booking.getId());

        partialUpdatedBooking
            .bookingId(UPDATED_BOOKING_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .roomId(UPDATED_ROOM_ID)
            .checkInDate(UPDATED_CHECK_IN_DATE)
            .checkOutDate(UPDATED_CHECK_OUT_DATE)
            .totalPrice(UPDATED_TOTAL_PRICE);

        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooking.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooking))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testBooking.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testBooking.getRoomId()).isEqualTo(UPDATED_ROOM_ID);
        assertThat(testBooking.getCheckInDate()).isEqualTo(UPDATED_CHECK_IN_DATE);
        assertThat(testBooking.getCheckOutDate()).isEqualTo(UPDATED_CHECK_OUT_DATE);
        assertThat(testBooking.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setId(UUID.randomUUID());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setId(UUID.randomUUID());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setId(UUID.randomUUID());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeDelete = bookingRepository.findAll().size();

        // Delete the booking
        restBookingMockMvc
            .perform(delete(ENTITY_API_URL_ID, booking.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
