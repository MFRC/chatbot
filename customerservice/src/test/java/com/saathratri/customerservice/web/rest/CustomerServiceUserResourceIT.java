package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.domain.CustomerService;
import com.saathratri.customerservice.domain.CustomerServiceUser;
import com.saathratri.customerservice.repository.CustomerServiceUserRepository;
import com.saathratri.customerservice.service.criteria.CustomerServiceUserCriteria;
import com.saathratri.customerservice.service.dto.CustomerServiceUserDTO;
import com.saathratri.customerservice.service.mapper.CustomerServiceUserMapper;
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
 * Integration tests for the {@link CustomerServiceUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerServiceUserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RESERVATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RESERVATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ROOM_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-service-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CustomerServiceUserRepository customerServiceUserRepository;

    @Autowired
    private CustomerServiceUserMapper customerServiceUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerServiceUserMockMvc;

    private CustomerServiceUser customerServiceUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerServiceUser createEntity(EntityManager em) {
        CustomerServiceUser customerServiceUser = new CustomerServiceUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .reservationNumber(DEFAULT_RESERVATION_NUMBER)
            .roomNumber(DEFAULT_ROOM_NUMBER);
        return customerServiceUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerServiceUser createUpdatedEntity(EntityManager em) {
        CustomerServiceUser customerServiceUser = new CustomerServiceUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .roomNumber(UPDATED_ROOM_NUMBER);
        return customerServiceUser;
    }

    @BeforeEach
    public void initTest() {
        customerServiceUser = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerServiceUser() throws Exception {
        int databaseSizeBeforeCreate = customerServiceUserRepository.findAll().size();
        // Create the CustomerServiceUser
        CustomerServiceUserDTO customerServiceUserDTO = customerServiceUserMapper.toDto(customerServiceUser);
        restCustomerServiceUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerServiceUser in the database
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerServiceUser testCustomerServiceUser = customerServiceUserList.get(customerServiceUserList.size() - 1);
        assertThat(testCustomerServiceUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCustomerServiceUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCustomerServiceUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomerServiceUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCustomerServiceUser.getReservationNumber()).isEqualTo(DEFAULT_RESERVATION_NUMBER);
        assertThat(testCustomerServiceUser.getRoomNumber()).isEqualTo(DEFAULT_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void createCustomerServiceUserWithExistingId() throws Exception {
        // Create the CustomerServiceUser with an existing ID
        customerServiceUserRepository.saveAndFlush(customerServiceUser);
        CustomerServiceUserDTO customerServiceUserDTO = customerServiceUserMapper.toDto(customerServiceUser);

        int databaseSizeBeforeCreate = customerServiceUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerServiceUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerServiceUser in the database
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsers() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList
        restCustomerServiceUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerServiceUser.getId().toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].reservationNumber").value(hasItem(DEFAULT_RESERVATION_NUMBER)))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerServiceUser() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get the customerServiceUser
        restCustomerServiceUserMockMvc
            .perform(get(ENTITY_API_URL_ID, customerServiceUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerServiceUser.getId().toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.reservationNumber").value(DEFAULT_RESERVATION_NUMBER))
            .andExpect(jsonPath("$.roomNumber").value(DEFAULT_ROOM_NUMBER));
    }

    @Test
    @Transactional
    void getCustomerServiceUsersByIdFiltering() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        UUID id = customerServiceUser.getId();

        defaultCustomerServiceUserShouldBeFound("id.equals=" + id);
        defaultCustomerServiceUserShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultCustomerServiceUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the customerServiceUserList where firstName equals to UPDATED_FIRST_NAME
        defaultCustomerServiceUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultCustomerServiceUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the customerServiceUserList where firstName equals to UPDATED_FIRST_NAME
        defaultCustomerServiceUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where firstName is not null
        defaultCustomerServiceUserShouldBeFound("firstName.specified=true");

        // Get all the customerServiceUserList where firstName is null
        defaultCustomerServiceUserShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where firstName contains DEFAULT_FIRST_NAME
        defaultCustomerServiceUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the customerServiceUserList where firstName contains UPDATED_FIRST_NAME
        defaultCustomerServiceUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultCustomerServiceUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the customerServiceUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultCustomerServiceUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where lastName equals to DEFAULT_LAST_NAME
        defaultCustomerServiceUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the customerServiceUserList where lastName equals to UPDATED_LAST_NAME
        defaultCustomerServiceUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultCustomerServiceUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the customerServiceUserList where lastName equals to UPDATED_LAST_NAME
        defaultCustomerServiceUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where lastName is not null
        defaultCustomerServiceUserShouldBeFound("lastName.specified=true");

        // Get all the customerServiceUserList where lastName is null
        defaultCustomerServiceUserShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where lastName contains DEFAULT_LAST_NAME
        defaultCustomerServiceUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the customerServiceUserList where lastName contains UPDATED_LAST_NAME
        defaultCustomerServiceUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultCustomerServiceUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the customerServiceUserList where lastName does not contain UPDATED_LAST_NAME
        defaultCustomerServiceUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where email equals to DEFAULT_EMAIL
        defaultCustomerServiceUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the customerServiceUserList where email equals to UPDATED_EMAIL
        defaultCustomerServiceUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCustomerServiceUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the customerServiceUserList where email equals to UPDATED_EMAIL
        defaultCustomerServiceUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where email is not null
        defaultCustomerServiceUserShouldBeFound("email.specified=true");

        // Get all the customerServiceUserList where email is null
        defaultCustomerServiceUserShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where email contains DEFAULT_EMAIL
        defaultCustomerServiceUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the customerServiceUserList where email contains UPDATED_EMAIL
        defaultCustomerServiceUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where email does not contain DEFAULT_EMAIL
        defaultCustomerServiceUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the customerServiceUserList where email does not contain UPDATED_EMAIL
        defaultCustomerServiceUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultCustomerServiceUserShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the customerServiceUserList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultCustomerServiceUserShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the customerServiceUserList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where phoneNumber is not null
        defaultCustomerServiceUserShouldBeFound("phoneNumber.specified=true");

        // Get all the customerServiceUserList where phoneNumber is null
        defaultCustomerServiceUserShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultCustomerServiceUserShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the customerServiceUserList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the customerServiceUserList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultCustomerServiceUserShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByReservationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where reservationNumber equals to DEFAULT_RESERVATION_NUMBER
        defaultCustomerServiceUserShouldBeFound("reservationNumber.equals=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the customerServiceUserList where reservationNumber equals to UPDATED_RESERVATION_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("reservationNumber.equals=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByReservationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where reservationNumber in DEFAULT_RESERVATION_NUMBER or UPDATED_RESERVATION_NUMBER
        defaultCustomerServiceUserShouldBeFound("reservationNumber.in=" + DEFAULT_RESERVATION_NUMBER + "," + UPDATED_RESERVATION_NUMBER);

        // Get all the customerServiceUserList where reservationNumber equals to UPDATED_RESERVATION_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("reservationNumber.in=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByReservationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where reservationNumber is not null
        defaultCustomerServiceUserShouldBeFound("reservationNumber.specified=true");

        // Get all the customerServiceUserList where reservationNumber is null
        defaultCustomerServiceUserShouldNotBeFound("reservationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByReservationNumberContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where reservationNumber contains DEFAULT_RESERVATION_NUMBER
        defaultCustomerServiceUserShouldBeFound("reservationNumber.contains=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the customerServiceUserList where reservationNumber contains UPDATED_RESERVATION_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("reservationNumber.contains=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByReservationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where reservationNumber does not contain DEFAULT_RESERVATION_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("reservationNumber.doesNotContain=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the customerServiceUserList where reservationNumber does not contain UPDATED_RESERVATION_NUMBER
        defaultCustomerServiceUserShouldBeFound("reservationNumber.doesNotContain=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByRoomNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where roomNumber equals to DEFAULT_ROOM_NUMBER
        defaultCustomerServiceUserShouldBeFound("roomNumber.equals=" + DEFAULT_ROOM_NUMBER);

        // Get all the customerServiceUserList where roomNumber equals to UPDATED_ROOM_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("roomNumber.equals=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByRoomNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where roomNumber in DEFAULT_ROOM_NUMBER or UPDATED_ROOM_NUMBER
        defaultCustomerServiceUserShouldBeFound("roomNumber.in=" + DEFAULT_ROOM_NUMBER + "," + UPDATED_ROOM_NUMBER);

        // Get all the customerServiceUserList where roomNumber equals to UPDATED_ROOM_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("roomNumber.in=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByRoomNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where roomNumber is not null
        defaultCustomerServiceUserShouldBeFound("roomNumber.specified=true");

        // Get all the customerServiceUserList where roomNumber is null
        defaultCustomerServiceUserShouldNotBeFound("roomNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByRoomNumberContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where roomNumber contains DEFAULT_ROOM_NUMBER
        defaultCustomerServiceUserShouldBeFound("roomNumber.contains=" + DEFAULT_ROOM_NUMBER);

        // Get all the customerServiceUserList where roomNumber contains UPDATED_ROOM_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("roomNumber.contains=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByRoomNumberNotContainsSomething() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        // Get all the customerServiceUserList where roomNumber does not contain DEFAULT_ROOM_NUMBER
        defaultCustomerServiceUserShouldNotBeFound("roomNumber.doesNotContain=" + DEFAULT_ROOM_NUMBER);

        // Get all the customerServiceUserList where roomNumber does not contain UPDATED_ROOM_NUMBER
        defaultCustomerServiceUserShouldBeFound("roomNumber.doesNotContain=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByConversationIsEqualToSomething() throws Exception {
        Conversation conversation;
        if (TestUtil.findAll(em, Conversation.class).isEmpty()) {
            customerServiceUserRepository.saveAndFlush(customerServiceUser);
            conversation = ConversationResourceIT.createEntity(em);
        } else {
            conversation = TestUtil.findAll(em, Conversation.class).get(0);
        }
        em.persist(conversation);
        em.flush();
        customerServiceUser.setConversation(conversation);
        customerServiceUserRepository.saveAndFlush(customerServiceUser);
        UUID conversationId = conversation.getId();

        // Get all the customerServiceUserList where conversation equals to conversationId
        defaultCustomerServiceUserShouldBeFound("conversationId.equals=" + conversationId);

        // Get all the customerServiceUserList where conversation equals to UUID.randomUUID()
        defaultCustomerServiceUserShouldNotBeFound("conversationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllCustomerServiceUsersByCustomerServiceIsEqualToSomething() throws Exception {
        CustomerService customerService;
        if (TestUtil.findAll(em, CustomerService.class).isEmpty()) {
            customerServiceUserRepository.saveAndFlush(customerServiceUser);
            customerService = CustomerServiceResourceIT.createEntity(em);
        } else {
            customerService = TestUtil.findAll(em, CustomerService.class).get(0);
        }
        em.persist(customerService);
        em.flush();
        customerServiceUser.setCustomerService(customerService);
        customerService.setCustomerServiceUser(customerServiceUser);
        customerServiceUserRepository.saveAndFlush(customerServiceUser);
        UUID customerServiceId = customerService.getId();

        // Get all the customerServiceUserList where customerService equals to customerServiceId
        defaultCustomerServiceUserShouldBeFound("customerServiceId.equals=" + customerServiceId);

        // Get all the customerServiceUserList where customerService equals to UUID.randomUUID()
        defaultCustomerServiceUserShouldNotBeFound("customerServiceId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerServiceUserShouldBeFound(String filter) throws Exception {
        restCustomerServiceUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerServiceUser.getId().toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].reservationNumber").value(hasItem(DEFAULT_RESERVATION_NUMBER)))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)));

        // Check, that the count call also returns 1
        restCustomerServiceUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerServiceUserShouldNotBeFound(String filter) throws Exception {
        restCustomerServiceUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerServiceUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerServiceUser() throws Exception {
        // Get the customerServiceUser
        restCustomerServiceUserMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerServiceUser() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        int databaseSizeBeforeUpdate = customerServiceUserRepository.findAll().size();

        // Update the customerServiceUser
        CustomerServiceUser updatedCustomerServiceUser = customerServiceUserRepository.findById(customerServiceUser.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerServiceUser are not directly saved in db
        em.detach(updatedCustomerServiceUser);
        updatedCustomerServiceUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .roomNumber(UPDATED_ROOM_NUMBER);
        CustomerServiceUserDTO customerServiceUserDTO = customerServiceUserMapper.toDto(updatedCustomerServiceUser);

        restCustomerServiceUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerServiceUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerServiceUser in the database
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeUpdate);
        CustomerServiceUser testCustomerServiceUser = customerServiceUserList.get(customerServiceUserList.size() - 1);
        assertThat(testCustomerServiceUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomerServiceUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomerServiceUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomerServiceUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCustomerServiceUser.getReservationNumber()).isEqualTo(UPDATED_RESERVATION_NUMBER);
        assertThat(testCustomerServiceUser.getRoomNumber()).isEqualTo(UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingCustomerServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceUserRepository.findAll().size();
        customerServiceUser.setId(UUID.randomUUID());

        // Create the CustomerServiceUser
        CustomerServiceUserDTO customerServiceUserDTO = customerServiceUserMapper.toDto(customerServiceUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerServiceUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerServiceUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerServiceUser in the database
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceUserRepository.findAll().size();
        customerServiceUser.setId(UUID.randomUUID());

        // Create the CustomerServiceUser
        CustomerServiceUserDTO customerServiceUserDTO = customerServiceUserMapper.toDto(customerServiceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerServiceUser in the database
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceUserRepository.findAll().size();
        customerServiceUser.setId(UUID.randomUUID());

        // Create the CustomerServiceUser
        CustomerServiceUserDTO customerServiceUserDTO = customerServiceUserMapper.toDto(customerServiceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceUserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerServiceUser in the database
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerServiceUserWithPatch() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        int databaseSizeBeforeUpdate = customerServiceUserRepository.findAll().size();

        // Update the customerServiceUser using partial update
        CustomerServiceUser partialUpdatedCustomerServiceUser = new CustomerServiceUser();
        partialUpdatedCustomerServiceUser.setId(customerServiceUser.getId());

        partialUpdatedCustomerServiceUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .reservationNumber(UPDATED_RESERVATION_NUMBER);

        restCustomerServiceUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerServiceUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerServiceUser))
            )
            .andExpect(status().isOk());

        // Validate the CustomerServiceUser in the database
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeUpdate);
        CustomerServiceUser testCustomerServiceUser = customerServiceUserList.get(customerServiceUserList.size() - 1);
        assertThat(testCustomerServiceUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomerServiceUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomerServiceUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomerServiceUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCustomerServiceUser.getReservationNumber()).isEqualTo(UPDATED_RESERVATION_NUMBER);
        assertThat(testCustomerServiceUser.getRoomNumber()).isEqualTo(DEFAULT_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateCustomerServiceUserWithPatch() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        int databaseSizeBeforeUpdate = customerServiceUserRepository.findAll().size();

        // Update the customerServiceUser using partial update
        CustomerServiceUser partialUpdatedCustomerServiceUser = new CustomerServiceUser();
        partialUpdatedCustomerServiceUser.setId(customerServiceUser.getId());

        partialUpdatedCustomerServiceUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .roomNumber(UPDATED_ROOM_NUMBER);

        restCustomerServiceUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerServiceUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerServiceUser))
            )
            .andExpect(status().isOk());

        // Validate the CustomerServiceUser in the database
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeUpdate);
        CustomerServiceUser testCustomerServiceUser = customerServiceUserList.get(customerServiceUserList.size() - 1);
        assertThat(testCustomerServiceUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomerServiceUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomerServiceUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomerServiceUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCustomerServiceUser.getReservationNumber()).isEqualTo(UPDATED_RESERVATION_NUMBER);
        assertThat(testCustomerServiceUser.getRoomNumber()).isEqualTo(UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceUserRepository.findAll().size();
        customerServiceUser.setId(UUID.randomUUID());

        // Create the CustomerServiceUser
        CustomerServiceUserDTO customerServiceUserDTO = customerServiceUserMapper.toDto(customerServiceUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerServiceUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerServiceUserDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerServiceUser in the database
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceUserRepository.findAll().size();
        customerServiceUser.setId(UUID.randomUUID());

        // Create the CustomerServiceUser
        CustomerServiceUserDTO customerServiceUserDTO = customerServiceUserMapper.toDto(customerServiceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerServiceUser in the database
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceUserRepository.findAll().size();
        customerServiceUser.setId(UUID.randomUUID());

        // Create the CustomerServiceUser
        CustomerServiceUserDTO customerServiceUserDTO = customerServiceUserMapper.toDto(customerServiceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerServiceUser in the database
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerServiceUser() throws Exception {
        // Initialize the database
        customerServiceUserRepository.saveAndFlush(customerServiceUser);

        int databaseSizeBeforeDelete = customerServiceUserRepository.findAll().size();

        // Delete the customerServiceUser
        restCustomerServiceUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerServiceUser.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerServiceUser> customerServiceUserList = customerServiceUserRepository.findAll();
        assertThat(customerServiceUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
