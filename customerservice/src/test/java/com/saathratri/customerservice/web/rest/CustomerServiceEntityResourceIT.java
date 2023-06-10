package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.domain.CustomerService;
import com.saathratri.customerservice.domain.CustomerServiceEntity;
import com.saathratri.customerservice.repository.CustomerServiceEntityRepository;
import com.saathratri.customerservice.service.criteria.CustomerServiceEntityCriteria;
import com.saathratri.customerservice.service.dto.CustomerServiceEntityDTO;
import com.saathratri.customerservice.service.mapper.CustomerServiceEntityMapper;
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
 * Integration tests for the {@link CustomerServiceEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerServiceEntityResourceIT {

    private static final String DEFAULT_RESERVATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RESERVATION_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_ROOM_NUMBER = 1;
    private static final Integer UPDATED_ROOM_NUMBER = 2;
    private static final Integer SMALLER_ROOM_NUMBER = 1 - 1;

    private static final String DEFAULT_SERVICES = "AAAAAAAAAA";
    private static final String UPDATED_SERVICES = "BBBBBBBBBB";

    private static final Long DEFAULT_PRICES = 1L;
    private static final Long UPDATED_PRICES = 2L;
    private static final Long SMALLER_PRICES = 1L - 1L;

    private static final String DEFAULT_AMENITIES = "AAAAAAAAAA";
    private static final String UPDATED_AMENITIES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-service-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CustomerServiceEntityRepository customerServiceEntityRepository;

    @Autowired
    private CustomerServiceEntityMapper customerServiceEntityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerServiceEntityMockMvc;

    private CustomerServiceEntity customerServiceEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerServiceEntity createEntity(EntityManager em) {
        CustomerServiceEntity customerServiceEntity = new CustomerServiceEntity()
            .reservationNumber(DEFAULT_RESERVATION_NUMBER)
            .roomNumber(DEFAULT_ROOM_NUMBER)
            .services(DEFAULT_SERVICES)
            .prices(DEFAULT_PRICES)
            .amenities(DEFAULT_AMENITIES);
        return customerServiceEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerServiceEntity createUpdatedEntity(EntityManager em) {
        CustomerServiceEntity customerServiceEntity = new CustomerServiceEntity()
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .roomNumber(UPDATED_ROOM_NUMBER)
            .services(UPDATED_SERVICES)
            .prices(UPDATED_PRICES)
            .amenities(UPDATED_AMENITIES);
        return customerServiceEntity;
    }

    @BeforeEach
    public void initTest() {
        customerServiceEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerServiceEntity() throws Exception {
        int databaseSizeBeforeCreate = customerServiceEntityRepository.findAll().size();
        // Create the CustomerServiceEntity
        CustomerServiceEntityDTO customerServiceEntityDTO = customerServiceEntityMapper.toDto(customerServiceEntity);
        restCustomerServiceEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceEntityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerServiceEntity in the database
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerServiceEntity testCustomerServiceEntity = customerServiceEntityList.get(customerServiceEntityList.size() - 1);
        assertThat(testCustomerServiceEntity.getReservationNumber()).isEqualTo(DEFAULT_RESERVATION_NUMBER);
        assertThat(testCustomerServiceEntity.getRoomNumber()).isEqualTo(DEFAULT_ROOM_NUMBER);
        assertThat(testCustomerServiceEntity.getServices()).isEqualTo(DEFAULT_SERVICES);
        assertThat(testCustomerServiceEntity.getPrices()).isEqualTo(DEFAULT_PRICES);
        assertThat(testCustomerServiceEntity.getAmenities()).isEqualTo(DEFAULT_AMENITIES);
    }

    @Test
    @Transactional
    void createCustomerServiceEntityWithExistingId() throws Exception {
        // Create the CustomerServiceEntity with an existing ID
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);
        CustomerServiceEntityDTO customerServiceEntityDTO = customerServiceEntityMapper.toDto(customerServiceEntity);

        int databaseSizeBeforeCreate = customerServiceEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerServiceEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerServiceEntity in the database
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntities() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList
        restCustomerServiceEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerServiceEntity.getId().toString())))
            .andExpect(jsonPath("$.[*].reservationNumber").value(hasItem(DEFAULT_RESERVATION_NUMBER)))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)))
            .andExpect(jsonPath("$.[*].services").value(hasItem(DEFAULT_SERVICES)))
            .andExpect(jsonPath("$.[*].prices").value(hasItem(DEFAULT_PRICES.intValue())))
            .andExpect(jsonPath("$.[*].amenities").value(hasItem(DEFAULT_AMENITIES)));
    }

    @Test
    @Transactional
    void getCustomerServiceEntity() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get the customerServiceEntity
        restCustomerServiceEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, customerServiceEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerServiceEntity.getId().toString()))
            .andExpect(jsonPath("$.reservationNumber").value(DEFAULT_RESERVATION_NUMBER))
            .andExpect(jsonPath("$.roomNumber").value(DEFAULT_ROOM_NUMBER))
            .andExpect(jsonPath("$.services").value(DEFAULT_SERVICES))
            .andExpect(jsonPath("$.prices").value(DEFAULT_PRICES.intValue()))
            .andExpect(jsonPath("$.amenities").value(DEFAULT_AMENITIES));
    }

    @Test
    @Transactional
    void getCustomerServiceEntitiesByIdFiltering() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        UUID id = customerServiceEntity.getId();

        defaultCustomerServiceEntityShouldBeFound("id.equals=" + id);
        defaultCustomerServiceEntityShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByReservationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where reservationNumber equals to DEFAULT_RESERVATION_NUMBER
        defaultCustomerServiceEntityShouldBeFound("reservationNumber.equals=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the customerServiceEntityList where reservationNumber equals to UPDATED_RESERVATION_NUMBER
        defaultCustomerServiceEntityShouldNotBeFound("reservationNumber.equals=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByReservationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where reservationNumber in DEFAULT_RESERVATION_NUMBER or UPDATED_RESERVATION_NUMBER
        defaultCustomerServiceEntityShouldBeFound("reservationNumber.in=" + DEFAULT_RESERVATION_NUMBER + "," + UPDATED_RESERVATION_NUMBER);

        // Get all the customerServiceEntityList where reservationNumber equals to UPDATED_RESERVATION_NUMBER
        defaultCustomerServiceEntityShouldNotBeFound("reservationNumber.in=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByReservationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where reservationNumber is not null
        defaultCustomerServiceEntityShouldBeFound("reservationNumber.specified=true");

        // Get all the customerServiceEntityList where reservationNumber is null
        defaultCustomerServiceEntityShouldNotBeFound("reservationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByReservationNumberContainsSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where reservationNumber contains DEFAULT_RESERVATION_NUMBER
        defaultCustomerServiceEntityShouldBeFound("reservationNumber.contains=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the customerServiceEntityList where reservationNumber contains UPDATED_RESERVATION_NUMBER
        defaultCustomerServiceEntityShouldNotBeFound("reservationNumber.contains=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByReservationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where reservationNumber does not contain DEFAULT_RESERVATION_NUMBER
        defaultCustomerServiceEntityShouldNotBeFound("reservationNumber.doesNotContain=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the customerServiceEntityList where reservationNumber does not contain UPDATED_RESERVATION_NUMBER
        defaultCustomerServiceEntityShouldBeFound("reservationNumber.doesNotContain=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByRoomNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where roomNumber equals to DEFAULT_ROOM_NUMBER
        defaultCustomerServiceEntityShouldBeFound("roomNumber.equals=" + DEFAULT_ROOM_NUMBER);

        // Get all the customerServiceEntityList where roomNumber equals to UPDATED_ROOM_NUMBER
        defaultCustomerServiceEntityShouldNotBeFound("roomNumber.equals=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByRoomNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where roomNumber in DEFAULT_ROOM_NUMBER or UPDATED_ROOM_NUMBER
        defaultCustomerServiceEntityShouldBeFound("roomNumber.in=" + DEFAULT_ROOM_NUMBER + "," + UPDATED_ROOM_NUMBER);

        // Get all the customerServiceEntityList where roomNumber equals to UPDATED_ROOM_NUMBER
        defaultCustomerServiceEntityShouldNotBeFound("roomNumber.in=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByRoomNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where roomNumber is not null
        defaultCustomerServiceEntityShouldBeFound("roomNumber.specified=true");

        // Get all the customerServiceEntityList where roomNumber is null
        defaultCustomerServiceEntityShouldNotBeFound("roomNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByRoomNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where roomNumber is greater than or equal to DEFAULT_ROOM_NUMBER
        defaultCustomerServiceEntityShouldBeFound("roomNumber.greaterThanOrEqual=" + DEFAULT_ROOM_NUMBER);

        // Get all the customerServiceEntityList where roomNumber is greater than or equal to UPDATED_ROOM_NUMBER
        defaultCustomerServiceEntityShouldNotBeFound("roomNumber.greaterThanOrEqual=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByRoomNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where roomNumber is less than or equal to DEFAULT_ROOM_NUMBER
        defaultCustomerServiceEntityShouldBeFound("roomNumber.lessThanOrEqual=" + DEFAULT_ROOM_NUMBER);

        // Get all the customerServiceEntityList where roomNumber is less than or equal to SMALLER_ROOM_NUMBER
        defaultCustomerServiceEntityShouldNotBeFound("roomNumber.lessThanOrEqual=" + SMALLER_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByRoomNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where roomNumber is less than DEFAULT_ROOM_NUMBER
        defaultCustomerServiceEntityShouldNotBeFound("roomNumber.lessThan=" + DEFAULT_ROOM_NUMBER);

        // Get all the customerServiceEntityList where roomNumber is less than UPDATED_ROOM_NUMBER
        defaultCustomerServiceEntityShouldBeFound("roomNumber.lessThan=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByRoomNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where roomNumber is greater than DEFAULT_ROOM_NUMBER
        defaultCustomerServiceEntityShouldNotBeFound("roomNumber.greaterThan=" + DEFAULT_ROOM_NUMBER);

        // Get all the customerServiceEntityList where roomNumber is greater than SMALLER_ROOM_NUMBER
        defaultCustomerServiceEntityShouldBeFound("roomNumber.greaterThan=" + SMALLER_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByServicesIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where services equals to DEFAULT_SERVICES
        defaultCustomerServiceEntityShouldBeFound("services.equals=" + DEFAULT_SERVICES);

        // Get all the customerServiceEntityList where services equals to UPDATED_SERVICES
        defaultCustomerServiceEntityShouldNotBeFound("services.equals=" + UPDATED_SERVICES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByServicesIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where services in DEFAULT_SERVICES or UPDATED_SERVICES
        defaultCustomerServiceEntityShouldBeFound("services.in=" + DEFAULT_SERVICES + "," + UPDATED_SERVICES);

        // Get all the customerServiceEntityList where services equals to UPDATED_SERVICES
        defaultCustomerServiceEntityShouldNotBeFound("services.in=" + UPDATED_SERVICES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByServicesIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where services is not null
        defaultCustomerServiceEntityShouldBeFound("services.specified=true");

        // Get all the customerServiceEntityList where services is null
        defaultCustomerServiceEntityShouldNotBeFound("services.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByServicesContainsSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where services contains DEFAULT_SERVICES
        defaultCustomerServiceEntityShouldBeFound("services.contains=" + DEFAULT_SERVICES);

        // Get all the customerServiceEntityList where services contains UPDATED_SERVICES
        defaultCustomerServiceEntityShouldNotBeFound("services.contains=" + UPDATED_SERVICES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByServicesNotContainsSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where services does not contain DEFAULT_SERVICES
        defaultCustomerServiceEntityShouldNotBeFound("services.doesNotContain=" + DEFAULT_SERVICES);

        // Get all the customerServiceEntityList where services does not contain UPDATED_SERVICES
        defaultCustomerServiceEntityShouldBeFound("services.doesNotContain=" + UPDATED_SERVICES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByPricesIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where prices equals to DEFAULT_PRICES
        defaultCustomerServiceEntityShouldBeFound("prices.equals=" + DEFAULT_PRICES);

        // Get all the customerServiceEntityList where prices equals to UPDATED_PRICES
        defaultCustomerServiceEntityShouldNotBeFound("prices.equals=" + UPDATED_PRICES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByPricesIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where prices in DEFAULT_PRICES or UPDATED_PRICES
        defaultCustomerServiceEntityShouldBeFound("prices.in=" + DEFAULT_PRICES + "," + UPDATED_PRICES);

        // Get all the customerServiceEntityList where prices equals to UPDATED_PRICES
        defaultCustomerServiceEntityShouldNotBeFound("prices.in=" + UPDATED_PRICES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByPricesIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where prices is not null
        defaultCustomerServiceEntityShouldBeFound("prices.specified=true");

        // Get all the customerServiceEntityList where prices is null
        defaultCustomerServiceEntityShouldNotBeFound("prices.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByPricesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where prices is greater than or equal to DEFAULT_PRICES
        defaultCustomerServiceEntityShouldBeFound("prices.greaterThanOrEqual=" + DEFAULT_PRICES);

        // Get all the customerServiceEntityList where prices is greater than or equal to UPDATED_PRICES
        defaultCustomerServiceEntityShouldNotBeFound("prices.greaterThanOrEqual=" + UPDATED_PRICES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByPricesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where prices is less than or equal to DEFAULT_PRICES
        defaultCustomerServiceEntityShouldBeFound("prices.lessThanOrEqual=" + DEFAULT_PRICES);

        // Get all the customerServiceEntityList where prices is less than or equal to SMALLER_PRICES
        defaultCustomerServiceEntityShouldNotBeFound("prices.lessThanOrEqual=" + SMALLER_PRICES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByPricesIsLessThanSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where prices is less than DEFAULT_PRICES
        defaultCustomerServiceEntityShouldNotBeFound("prices.lessThan=" + DEFAULT_PRICES);

        // Get all the customerServiceEntityList where prices is less than UPDATED_PRICES
        defaultCustomerServiceEntityShouldBeFound("prices.lessThan=" + UPDATED_PRICES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByPricesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where prices is greater than DEFAULT_PRICES
        defaultCustomerServiceEntityShouldNotBeFound("prices.greaterThan=" + DEFAULT_PRICES);

        // Get all the customerServiceEntityList where prices is greater than SMALLER_PRICES
        defaultCustomerServiceEntityShouldBeFound("prices.greaterThan=" + SMALLER_PRICES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByAmenitiesIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where amenities equals to DEFAULT_AMENITIES
        defaultCustomerServiceEntityShouldBeFound("amenities.equals=" + DEFAULT_AMENITIES);

        // Get all the customerServiceEntityList where amenities equals to UPDATED_AMENITIES
        defaultCustomerServiceEntityShouldNotBeFound("amenities.equals=" + UPDATED_AMENITIES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByAmenitiesIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where amenities in DEFAULT_AMENITIES or UPDATED_AMENITIES
        defaultCustomerServiceEntityShouldBeFound("amenities.in=" + DEFAULT_AMENITIES + "," + UPDATED_AMENITIES);

        // Get all the customerServiceEntityList where amenities equals to UPDATED_AMENITIES
        defaultCustomerServiceEntityShouldNotBeFound("amenities.in=" + UPDATED_AMENITIES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByAmenitiesIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where amenities is not null
        defaultCustomerServiceEntityShouldBeFound("amenities.specified=true");

        // Get all the customerServiceEntityList where amenities is null
        defaultCustomerServiceEntityShouldNotBeFound("amenities.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByAmenitiesContainsSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where amenities contains DEFAULT_AMENITIES
        defaultCustomerServiceEntityShouldBeFound("amenities.contains=" + DEFAULT_AMENITIES);

        // Get all the customerServiceEntityList where amenities contains UPDATED_AMENITIES
        defaultCustomerServiceEntityShouldNotBeFound("amenities.contains=" + UPDATED_AMENITIES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByAmenitiesNotContainsSomething() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        // Get all the customerServiceEntityList where amenities does not contain DEFAULT_AMENITIES
        defaultCustomerServiceEntityShouldNotBeFound("amenities.doesNotContain=" + DEFAULT_AMENITIES);

        // Get all the customerServiceEntityList where amenities does not contain UPDATED_AMENITIES
        defaultCustomerServiceEntityShouldBeFound("amenities.doesNotContain=" + UPDATED_AMENITIES);
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByConversationIsEqualToSomething() throws Exception {
        Conversation conversation;
        if (TestUtil.findAll(em, Conversation.class).isEmpty()) {
            customerServiceEntityRepository.saveAndFlush(customerServiceEntity);
            conversation = ConversationResourceIT.createEntity(em);
        } else {
            conversation = TestUtil.findAll(em, Conversation.class).get(0);
        }
        em.persist(conversation);
        em.flush();
        customerServiceEntity.setConversation(conversation);
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);
        UUID conversationId = conversation.getId();

        // Get all the customerServiceEntityList where conversation equals to conversationId
        defaultCustomerServiceEntityShouldBeFound("conversationId.equals=" + conversationId);

        // Get all the customerServiceEntityList where conversation equals to UUID.randomUUID()
        defaultCustomerServiceEntityShouldNotBeFound("conversationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllCustomerServiceEntitiesByCustomerServiceIsEqualToSomething() throws Exception {
        CustomerService customerService;
        if (TestUtil.findAll(em, CustomerService.class).isEmpty()) {
            customerServiceEntityRepository.saveAndFlush(customerServiceEntity);
            customerService = CustomerServiceResourceIT.createEntity(em);
        } else {
            customerService = TestUtil.findAll(em, CustomerService.class).get(0);
        }
        em.persist(customerService);
        em.flush();
        customerServiceEntity.setCustomerService(customerService);
        customerService.setCustomerServiceEntity(customerServiceEntity);
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);
        UUID customerServiceId = customerService.getId();

        // Get all the customerServiceEntityList where customerService equals to customerServiceId
        defaultCustomerServiceEntityShouldBeFound("customerServiceId.equals=" + customerServiceId);

        // Get all the customerServiceEntityList where customerService equals to UUID.randomUUID()
        defaultCustomerServiceEntityShouldNotBeFound("customerServiceId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerServiceEntityShouldBeFound(String filter) throws Exception {
        restCustomerServiceEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerServiceEntity.getId().toString())))
            .andExpect(jsonPath("$.[*].reservationNumber").value(hasItem(DEFAULT_RESERVATION_NUMBER)))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)))
            .andExpect(jsonPath("$.[*].services").value(hasItem(DEFAULT_SERVICES)))
            .andExpect(jsonPath("$.[*].prices").value(hasItem(DEFAULT_PRICES.intValue())))
            .andExpect(jsonPath("$.[*].amenities").value(hasItem(DEFAULT_AMENITIES)));

        // Check, that the count call also returns 1
        restCustomerServiceEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerServiceEntityShouldNotBeFound(String filter) throws Exception {
        restCustomerServiceEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerServiceEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerServiceEntity() throws Exception {
        // Get the customerServiceEntity
        restCustomerServiceEntityMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerServiceEntity() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        int databaseSizeBeforeUpdate = customerServiceEntityRepository.findAll().size();

        // Update the customerServiceEntity
        CustomerServiceEntity updatedCustomerServiceEntity = customerServiceEntityRepository.findById(customerServiceEntity.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerServiceEntity are not directly saved in db
        em.detach(updatedCustomerServiceEntity);
        updatedCustomerServiceEntity
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .roomNumber(UPDATED_ROOM_NUMBER)
            .services(UPDATED_SERVICES)
            .prices(UPDATED_PRICES)
            .amenities(UPDATED_AMENITIES);
        CustomerServiceEntityDTO customerServiceEntityDTO = customerServiceEntityMapper.toDto(updatedCustomerServiceEntity);

        restCustomerServiceEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerServiceEntityDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceEntityDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerServiceEntity in the database
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeUpdate);
        CustomerServiceEntity testCustomerServiceEntity = customerServiceEntityList.get(customerServiceEntityList.size() - 1);
        assertThat(testCustomerServiceEntity.getReservationNumber()).isEqualTo(UPDATED_RESERVATION_NUMBER);
        assertThat(testCustomerServiceEntity.getRoomNumber()).isEqualTo(UPDATED_ROOM_NUMBER);
        assertThat(testCustomerServiceEntity.getServices()).isEqualTo(UPDATED_SERVICES);
        assertThat(testCustomerServiceEntity.getPrices()).isEqualTo(UPDATED_PRICES);
        assertThat(testCustomerServiceEntity.getAmenities()).isEqualTo(UPDATED_AMENITIES);
    }

    @Test
    @Transactional
    void putNonExistingCustomerServiceEntity() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceEntityRepository.findAll().size();
        customerServiceEntity.setId(UUID.randomUUID());

        // Create the CustomerServiceEntity
        CustomerServiceEntityDTO customerServiceEntityDTO = customerServiceEntityMapper.toDto(customerServiceEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerServiceEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerServiceEntityDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerServiceEntity in the database
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerServiceEntity() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceEntityRepository.findAll().size();
        customerServiceEntity.setId(UUID.randomUUID());

        // Create the CustomerServiceEntity
        CustomerServiceEntityDTO customerServiceEntityDTO = customerServiceEntityMapper.toDto(customerServiceEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerServiceEntity in the database
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerServiceEntity() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceEntityRepository.findAll().size();
        customerServiceEntity.setId(UUID.randomUUID());

        // Create the CustomerServiceEntity
        CustomerServiceEntityDTO customerServiceEntityDTO = customerServiceEntityMapper.toDto(customerServiceEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceEntityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerServiceEntity in the database
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerServiceEntityWithPatch() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        int databaseSizeBeforeUpdate = customerServiceEntityRepository.findAll().size();

        // Update the customerServiceEntity using partial update
        CustomerServiceEntity partialUpdatedCustomerServiceEntity = new CustomerServiceEntity();
        partialUpdatedCustomerServiceEntity.setId(customerServiceEntity.getId());

        partialUpdatedCustomerServiceEntity.roomNumber(UPDATED_ROOM_NUMBER).prices(UPDATED_PRICES);

        restCustomerServiceEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerServiceEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerServiceEntity))
            )
            .andExpect(status().isOk());

        // Validate the CustomerServiceEntity in the database
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeUpdate);
        CustomerServiceEntity testCustomerServiceEntity = customerServiceEntityList.get(customerServiceEntityList.size() - 1);
        assertThat(testCustomerServiceEntity.getReservationNumber()).isEqualTo(DEFAULT_RESERVATION_NUMBER);
        assertThat(testCustomerServiceEntity.getRoomNumber()).isEqualTo(UPDATED_ROOM_NUMBER);
        assertThat(testCustomerServiceEntity.getServices()).isEqualTo(DEFAULT_SERVICES);
        assertThat(testCustomerServiceEntity.getPrices()).isEqualTo(UPDATED_PRICES);
        assertThat(testCustomerServiceEntity.getAmenities()).isEqualTo(DEFAULT_AMENITIES);
    }

    @Test
    @Transactional
    void fullUpdateCustomerServiceEntityWithPatch() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        int databaseSizeBeforeUpdate = customerServiceEntityRepository.findAll().size();

        // Update the customerServiceEntity using partial update
        CustomerServiceEntity partialUpdatedCustomerServiceEntity = new CustomerServiceEntity();
        partialUpdatedCustomerServiceEntity.setId(customerServiceEntity.getId());

        partialUpdatedCustomerServiceEntity
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .roomNumber(UPDATED_ROOM_NUMBER)
            .services(UPDATED_SERVICES)
            .prices(UPDATED_PRICES)
            .amenities(UPDATED_AMENITIES);

        restCustomerServiceEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerServiceEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerServiceEntity))
            )
            .andExpect(status().isOk());

        // Validate the CustomerServiceEntity in the database
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeUpdate);
        CustomerServiceEntity testCustomerServiceEntity = customerServiceEntityList.get(customerServiceEntityList.size() - 1);
        assertThat(testCustomerServiceEntity.getReservationNumber()).isEqualTo(UPDATED_RESERVATION_NUMBER);
        assertThat(testCustomerServiceEntity.getRoomNumber()).isEqualTo(UPDATED_ROOM_NUMBER);
        assertThat(testCustomerServiceEntity.getServices()).isEqualTo(UPDATED_SERVICES);
        assertThat(testCustomerServiceEntity.getPrices()).isEqualTo(UPDATED_PRICES);
        assertThat(testCustomerServiceEntity.getAmenities()).isEqualTo(UPDATED_AMENITIES);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerServiceEntity() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceEntityRepository.findAll().size();
        customerServiceEntity.setId(UUID.randomUUID());

        // Create the CustomerServiceEntity
        CustomerServiceEntityDTO customerServiceEntityDTO = customerServiceEntityMapper.toDto(customerServiceEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerServiceEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerServiceEntityDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerServiceEntity in the database
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerServiceEntity() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceEntityRepository.findAll().size();
        customerServiceEntity.setId(UUID.randomUUID());

        // Create the CustomerServiceEntity
        CustomerServiceEntityDTO customerServiceEntityDTO = customerServiceEntityMapper.toDto(customerServiceEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerServiceEntity in the database
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerServiceEntity() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceEntityRepository.findAll().size();
        customerServiceEntity.setId(UUID.randomUUID());

        // Create the CustomerServiceEntity
        CustomerServiceEntityDTO customerServiceEntityDTO = customerServiceEntityMapper.toDto(customerServiceEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerServiceEntity in the database
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerServiceEntity() throws Exception {
        // Initialize the database
        customerServiceEntityRepository.saveAndFlush(customerServiceEntity);

        int databaseSizeBeforeDelete = customerServiceEntityRepository.findAll().size();

        // Delete the customerServiceEntity
        restCustomerServiceEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerServiceEntity.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerServiceEntity> customerServiceEntityList = customerServiceEntityRepository.findAll();
        assertThat(customerServiceEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
