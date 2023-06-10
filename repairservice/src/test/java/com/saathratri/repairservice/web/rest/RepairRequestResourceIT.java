package com.saathratri.repairservice.web.rest;

import static com.saathratri.repairservice.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.repairservice.IntegrationTest;
import com.saathratri.repairservice.domain.Customer;
import com.saathratri.repairservice.domain.RepairRequest;
import com.saathratri.repairservice.domain.enumeration.RepairStatus;
import com.saathratri.repairservice.repository.RepairRequestRepository;
import com.saathratri.repairservice.service.criteria.RepairRequestCriteria;
import com.saathratri.repairservice.service.dto.RepairRequestDTO;
import com.saathratri.repairservice.service.mapper.RepairRequestMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link RepairRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RepairRequestResourceIT {

    private static final UUID DEFAULT_REPAIR_REQUEST_ID = UUID.randomUUID();
    private static final UUID UPDATED_REPAIR_REQUEST_ID = UUID.randomUUID();

    private static final UUID DEFAULT_CUSTOMER_ID = UUID.randomUUID();
    private static final UUID UPDATED_CUSTOMER_ID = UUID.randomUUID();

    private static final String DEFAULT_ROOM_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final RepairStatus DEFAULT_STATUS = RepairStatus.PENDING;
    private static final RepairStatus UPDATED_STATUS = RepairStatus.IN_PROGRESS;

    private static final ZonedDateTime DEFAULT_DATE_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATE_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/repair-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    @Autowired
    private RepairRequestMapper repairRequestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRepairRequestMockMvc;

    private RepairRequest repairRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RepairRequest createEntity(EntityManager em) {
        RepairRequest repairRequest = new RepairRequest()
            .repairRequestId(DEFAULT_REPAIR_REQUEST_ID)
            .customerId(DEFAULT_CUSTOMER_ID)
            .roomNumber(DEFAULT_ROOM_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED);
        return repairRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RepairRequest createUpdatedEntity(EntityManager em) {
        RepairRequest repairRequest = new RepairRequest()
            .repairRequestId(UPDATED_REPAIR_REQUEST_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .roomNumber(UPDATED_ROOM_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        return repairRequest;
    }

    @BeforeEach
    public void initTest() {
        repairRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createRepairRequest() throws Exception {
        int databaseSizeBeforeCreate = repairRequestRepository.findAll().size();
        // Create the RepairRequest
        RepairRequestDTO repairRequestDTO = repairRequestMapper.toDto(repairRequest);
        restRepairRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repairRequestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RepairRequest in the database
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeCreate + 1);
        RepairRequest testRepairRequest = repairRequestList.get(repairRequestList.size() - 1);
        assertThat(testRepairRequest.getRepairRequestId()).isEqualTo(DEFAULT_REPAIR_REQUEST_ID);
        assertThat(testRepairRequest.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testRepairRequest.getRoomNumber()).isEqualTo(DEFAULT_ROOM_NUMBER);
        assertThat(testRepairRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRepairRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRepairRequest.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testRepairRequest.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
    }

    @Test
    @Transactional
    void createRepairRequestWithExistingId() throws Exception {
        // Create the RepairRequest with an existing ID
        repairRequestRepository.saveAndFlush(repairRequest);
        RepairRequestDTO repairRequestDTO = repairRequestMapper.toDto(repairRequest);

        int databaseSizeBeforeCreate = repairRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepairRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repairRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RepairRequest in the database
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRepairRequests() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList
        restRepairRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repairRequest.getId().toString())))
            .andExpect(jsonPath("$.[*].repairRequestId").value(hasItem(DEFAULT_REPAIR_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.toString())))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(sameInstant(DEFAULT_DATE_CREATED))))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(sameInstant(DEFAULT_DATE_UPDATED))));
    }

    @Test
    @Transactional
    void getRepairRequest() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get the repairRequest
        restRepairRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, repairRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(repairRequest.getId().toString()))
            .andExpect(jsonPath("$.repairRequestId").value(DEFAULT_REPAIR_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.toString()))
            .andExpect(jsonPath("$.roomNumber").value(DEFAULT_ROOM_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dateCreated").value(sameInstant(DEFAULT_DATE_CREATED)))
            .andExpect(jsonPath("$.dateUpdated").value(sameInstant(DEFAULT_DATE_UPDATED)));
    }

    @Test
    @Transactional
    void getRepairRequestsByIdFiltering() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        UUID id = repairRequest.getId();

        defaultRepairRequestShouldBeFound("id.equals=" + id);
        defaultRepairRequestShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByRepairRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where repairRequestId equals to DEFAULT_REPAIR_REQUEST_ID
        defaultRepairRequestShouldBeFound("repairRequestId.equals=" + DEFAULT_REPAIR_REQUEST_ID);

        // Get all the repairRequestList where repairRequestId equals to UPDATED_REPAIR_REQUEST_ID
        defaultRepairRequestShouldNotBeFound("repairRequestId.equals=" + UPDATED_REPAIR_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByRepairRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where repairRequestId in DEFAULT_REPAIR_REQUEST_ID or UPDATED_REPAIR_REQUEST_ID
        defaultRepairRequestShouldBeFound("repairRequestId.in=" + DEFAULT_REPAIR_REQUEST_ID + "," + UPDATED_REPAIR_REQUEST_ID);

        // Get all the repairRequestList where repairRequestId equals to UPDATED_REPAIR_REQUEST_ID
        defaultRepairRequestShouldNotBeFound("repairRequestId.in=" + UPDATED_REPAIR_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByRepairRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where repairRequestId is not null
        defaultRepairRequestShouldBeFound("repairRequestId.specified=true");

        // Get all the repairRequestList where repairRequestId is null
        defaultRepairRequestShouldNotBeFound("repairRequestId.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairRequestsByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultRepairRequestShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the repairRequestList where customerId equals to UPDATED_CUSTOMER_ID
        defaultRepairRequestShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultRepairRequestShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the repairRequestList where customerId equals to UPDATED_CUSTOMER_ID
        defaultRepairRequestShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where customerId is not null
        defaultRepairRequestShouldBeFound("customerId.specified=true");

        // Get all the repairRequestList where customerId is null
        defaultRepairRequestShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairRequestsByRoomNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where roomNumber equals to DEFAULT_ROOM_NUMBER
        defaultRepairRequestShouldBeFound("roomNumber.equals=" + DEFAULT_ROOM_NUMBER);

        // Get all the repairRequestList where roomNumber equals to UPDATED_ROOM_NUMBER
        defaultRepairRequestShouldNotBeFound("roomNumber.equals=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByRoomNumberIsInShouldWork() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where roomNumber in DEFAULT_ROOM_NUMBER or UPDATED_ROOM_NUMBER
        defaultRepairRequestShouldBeFound("roomNumber.in=" + DEFAULT_ROOM_NUMBER + "," + UPDATED_ROOM_NUMBER);

        // Get all the repairRequestList where roomNumber equals to UPDATED_ROOM_NUMBER
        defaultRepairRequestShouldNotBeFound("roomNumber.in=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByRoomNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where roomNumber is not null
        defaultRepairRequestShouldBeFound("roomNumber.specified=true");

        // Get all the repairRequestList where roomNumber is null
        defaultRepairRequestShouldNotBeFound("roomNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairRequestsByRoomNumberContainsSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where roomNumber contains DEFAULT_ROOM_NUMBER
        defaultRepairRequestShouldBeFound("roomNumber.contains=" + DEFAULT_ROOM_NUMBER);

        // Get all the repairRequestList where roomNumber contains UPDATED_ROOM_NUMBER
        defaultRepairRequestShouldNotBeFound("roomNumber.contains=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByRoomNumberNotContainsSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where roomNumber does not contain DEFAULT_ROOM_NUMBER
        defaultRepairRequestShouldNotBeFound("roomNumber.doesNotContain=" + DEFAULT_ROOM_NUMBER);

        // Get all the repairRequestList where roomNumber does not contain UPDATED_ROOM_NUMBER
        defaultRepairRequestShouldBeFound("roomNumber.doesNotContain=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where description equals to DEFAULT_DESCRIPTION
        defaultRepairRequestShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the repairRequestList where description equals to UPDATED_DESCRIPTION
        defaultRepairRequestShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRepairRequestShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the repairRequestList where description equals to UPDATED_DESCRIPTION
        defaultRepairRequestShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where description is not null
        defaultRepairRequestShouldBeFound("description.specified=true");

        // Get all the repairRequestList where description is null
        defaultRepairRequestShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where description contains DEFAULT_DESCRIPTION
        defaultRepairRequestShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the repairRequestList where description contains UPDATED_DESCRIPTION
        defaultRepairRequestShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where description does not contain DEFAULT_DESCRIPTION
        defaultRepairRequestShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the repairRequestList where description does not contain UPDATED_DESCRIPTION
        defaultRepairRequestShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where status equals to DEFAULT_STATUS
        defaultRepairRequestShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the repairRequestList where status equals to UPDATED_STATUS
        defaultRepairRequestShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultRepairRequestShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the repairRequestList where status equals to UPDATED_STATUS
        defaultRepairRequestShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where status is not null
        defaultRepairRequestShouldBeFound("status.specified=true");

        // Get all the repairRequestList where status is null
        defaultRepairRequestShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultRepairRequestShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the repairRequestList where dateCreated equals to UPDATED_DATE_CREATED
        defaultRepairRequestShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultRepairRequestShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the repairRequestList where dateCreated equals to UPDATED_DATE_CREATED
        defaultRepairRequestShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateCreated is not null
        defaultRepairRequestShouldBeFound("dateCreated.specified=true");

        // Get all the repairRequestList where dateCreated is null
        defaultRepairRequestShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateCreated is greater than or equal to DEFAULT_DATE_CREATED
        defaultRepairRequestShouldBeFound("dateCreated.greaterThanOrEqual=" + DEFAULT_DATE_CREATED);

        // Get all the repairRequestList where dateCreated is greater than or equal to UPDATED_DATE_CREATED
        defaultRepairRequestShouldNotBeFound("dateCreated.greaterThanOrEqual=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateCreated is less than or equal to DEFAULT_DATE_CREATED
        defaultRepairRequestShouldBeFound("dateCreated.lessThanOrEqual=" + DEFAULT_DATE_CREATED);

        // Get all the repairRequestList where dateCreated is less than or equal to SMALLER_DATE_CREATED
        defaultRepairRequestShouldNotBeFound("dateCreated.lessThanOrEqual=" + SMALLER_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateCreated is less than DEFAULT_DATE_CREATED
        defaultRepairRequestShouldNotBeFound("dateCreated.lessThan=" + DEFAULT_DATE_CREATED);

        // Get all the repairRequestList where dateCreated is less than UPDATED_DATE_CREATED
        defaultRepairRequestShouldBeFound("dateCreated.lessThan=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateCreated is greater than DEFAULT_DATE_CREATED
        defaultRepairRequestShouldNotBeFound("dateCreated.greaterThan=" + DEFAULT_DATE_CREATED);

        // Get all the repairRequestList where dateCreated is greater than SMALLER_DATE_CREATED
        defaultRepairRequestShouldBeFound("dateCreated.greaterThan=" + SMALLER_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateUpdated equals to DEFAULT_DATE_UPDATED
        defaultRepairRequestShouldBeFound("dateUpdated.equals=" + DEFAULT_DATE_UPDATED);

        // Get all the repairRequestList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultRepairRequestShouldNotBeFound("dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateUpdated in DEFAULT_DATE_UPDATED or UPDATED_DATE_UPDATED
        defaultRepairRequestShouldBeFound("dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED);

        // Get all the repairRequestList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultRepairRequestShouldNotBeFound("dateUpdated.in=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateUpdated is not null
        defaultRepairRequestShouldBeFound("dateUpdated.specified=true");

        // Get all the repairRequestList where dateUpdated is null
        defaultRepairRequestShouldNotBeFound("dateUpdated.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateUpdatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateUpdated is greater than or equal to DEFAULT_DATE_UPDATED
        defaultRepairRequestShouldBeFound("dateUpdated.greaterThanOrEqual=" + DEFAULT_DATE_UPDATED);

        // Get all the repairRequestList where dateUpdated is greater than or equal to UPDATED_DATE_UPDATED
        defaultRepairRequestShouldNotBeFound("dateUpdated.greaterThanOrEqual=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateUpdatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateUpdated is less than or equal to DEFAULT_DATE_UPDATED
        defaultRepairRequestShouldBeFound("dateUpdated.lessThanOrEqual=" + DEFAULT_DATE_UPDATED);

        // Get all the repairRequestList where dateUpdated is less than or equal to SMALLER_DATE_UPDATED
        defaultRepairRequestShouldNotBeFound("dateUpdated.lessThanOrEqual=" + SMALLER_DATE_UPDATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateUpdatedIsLessThanSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateUpdated is less than DEFAULT_DATE_UPDATED
        defaultRepairRequestShouldNotBeFound("dateUpdated.lessThan=" + DEFAULT_DATE_UPDATED);

        // Get all the repairRequestList where dateUpdated is less than UPDATED_DATE_UPDATED
        defaultRepairRequestShouldBeFound("dateUpdated.lessThan=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByDateUpdatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        // Get all the repairRequestList where dateUpdated is greater than DEFAULT_DATE_UPDATED
        defaultRepairRequestShouldNotBeFound("dateUpdated.greaterThan=" + DEFAULT_DATE_UPDATED);

        // Get all the repairRequestList where dateUpdated is greater than SMALLER_DATE_UPDATED
        defaultRepairRequestShouldBeFound("dateUpdated.greaterThan=" + SMALLER_DATE_UPDATED);
    }

    @Test
    @Transactional
    void getAllRepairRequestsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            repairRequestRepository.saveAndFlush(repairRequest);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        repairRequest.setCustomer(customer);
        repairRequestRepository.saveAndFlush(repairRequest);
        UUID customerId = customer.getId();

        // Get all the repairRequestList where customer equals to customerId
        defaultRepairRequestShouldBeFound("customerId.equals=" + customerId);

        // Get all the repairRequestList where customer equals to UUID.randomUUID()
        defaultRepairRequestShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRepairRequestShouldBeFound(String filter) throws Exception {
        restRepairRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repairRequest.getId().toString())))
            .andExpect(jsonPath("$.[*].repairRequestId").value(hasItem(DEFAULT_REPAIR_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.toString())))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(sameInstant(DEFAULT_DATE_CREATED))))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(sameInstant(DEFAULT_DATE_UPDATED))));

        // Check, that the count call also returns 1
        restRepairRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRepairRequestShouldNotBeFound(String filter) throws Exception {
        restRepairRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRepairRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRepairRequest() throws Exception {
        // Get the repairRequest
        restRepairRequestMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRepairRequest() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        int databaseSizeBeforeUpdate = repairRequestRepository.findAll().size();

        // Update the repairRequest
        RepairRequest updatedRepairRequest = repairRequestRepository.findById(repairRequest.getId()).get();
        // Disconnect from session so that the updates on updatedRepairRequest are not directly saved in db
        em.detach(updatedRepairRequest);
        updatedRepairRequest
            .repairRequestId(UPDATED_REPAIR_REQUEST_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .roomNumber(UPDATED_ROOM_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        RepairRequestDTO repairRequestDTO = repairRequestMapper.toDto(updatedRepairRequest);

        restRepairRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repairRequestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repairRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the RepairRequest in the database
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeUpdate);
        RepairRequest testRepairRequest = repairRequestList.get(repairRequestList.size() - 1);
        assertThat(testRepairRequest.getRepairRequestId()).isEqualTo(UPDATED_REPAIR_REQUEST_ID);
        assertThat(testRepairRequest.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testRepairRequest.getRoomNumber()).isEqualTo(UPDATED_ROOM_NUMBER);
        assertThat(testRepairRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRepairRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRepairRequest.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testRepairRequest.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    void putNonExistingRepairRequest() throws Exception {
        int databaseSizeBeforeUpdate = repairRequestRepository.findAll().size();
        repairRequest.setId(UUID.randomUUID());

        // Create the RepairRequest
        RepairRequestDTO repairRequestDTO = repairRequestMapper.toDto(repairRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepairRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repairRequestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repairRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RepairRequest in the database
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRepairRequest() throws Exception {
        int databaseSizeBeforeUpdate = repairRequestRepository.findAll().size();
        repairRequest.setId(UUID.randomUUID());

        // Create the RepairRequest
        RepairRequestDTO repairRequestDTO = repairRequestMapper.toDto(repairRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repairRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RepairRequest in the database
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRepairRequest() throws Exception {
        int databaseSizeBeforeUpdate = repairRequestRepository.findAll().size();
        repairRequest.setId(UUID.randomUUID());

        // Create the RepairRequest
        RepairRequestDTO repairRequestDTO = repairRequestMapper.toDto(repairRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairRequestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repairRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RepairRequest in the database
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRepairRequestWithPatch() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        int databaseSizeBeforeUpdate = repairRequestRepository.findAll().size();

        // Update the repairRequest using partial update
        RepairRequest partialUpdatedRepairRequest = new RepairRequest();
        partialUpdatedRepairRequest.setId(repairRequest.getId());

        partialUpdatedRepairRequest.repairRequestId(UPDATED_REPAIR_REQUEST_ID).customerId(UPDATED_CUSTOMER_ID);

        restRepairRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepairRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepairRequest))
            )
            .andExpect(status().isOk());

        // Validate the RepairRequest in the database
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeUpdate);
        RepairRequest testRepairRequest = repairRequestList.get(repairRequestList.size() - 1);
        assertThat(testRepairRequest.getRepairRequestId()).isEqualTo(UPDATED_REPAIR_REQUEST_ID);
        assertThat(testRepairRequest.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testRepairRequest.getRoomNumber()).isEqualTo(DEFAULT_ROOM_NUMBER);
        assertThat(testRepairRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRepairRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRepairRequest.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testRepairRequest.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
    }

    @Test
    @Transactional
    void fullUpdateRepairRequestWithPatch() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        int databaseSizeBeforeUpdate = repairRequestRepository.findAll().size();

        // Update the repairRequest using partial update
        RepairRequest partialUpdatedRepairRequest = new RepairRequest();
        partialUpdatedRepairRequest.setId(repairRequest.getId());

        partialUpdatedRepairRequest
            .repairRequestId(UPDATED_REPAIR_REQUEST_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .roomNumber(UPDATED_ROOM_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);

        restRepairRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepairRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepairRequest))
            )
            .andExpect(status().isOk());

        // Validate the RepairRequest in the database
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeUpdate);
        RepairRequest testRepairRequest = repairRequestList.get(repairRequestList.size() - 1);
        assertThat(testRepairRequest.getRepairRequestId()).isEqualTo(UPDATED_REPAIR_REQUEST_ID);
        assertThat(testRepairRequest.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testRepairRequest.getRoomNumber()).isEqualTo(UPDATED_ROOM_NUMBER);
        assertThat(testRepairRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRepairRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRepairRequest.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testRepairRequest.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    void patchNonExistingRepairRequest() throws Exception {
        int databaseSizeBeforeUpdate = repairRequestRepository.findAll().size();
        repairRequest.setId(UUID.randomUUID());

        // Create the RepairRequest
        RepairRequestDTO repairRequestDTO = repairRequestMapper.toDto(repairRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepairRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, repairRequestDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repairRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RepairRequest in the database
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRepairRequest() throws Exception {
        int databaseSizeBeforeUpdate = repairRequestRepository.findAll().size();
        repairRequest.setId(UUID.randomUUID());

        // Create the RepairRequest
        RepairRequestDTO repairRequestDTO = repairRequestMapper.toDto(repairRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repairRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RepairRequest in the database
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRepairRequest() throws Exception {
        int databaseSizeBeforeUpdate = repairRequestRepository.findAll().size();
        repairRequest.setId(UUID.randomUUID());

        // Create the RepairRequest
        RepairRequestDTO repairRequestDTO = repairRequestMapper.toDto(repairRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repairRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RepairRequest in the database
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRepairRequest() throws Exception {
        // Initialize the database
        repairRequestRepository.saveAndFlush(repairRequest);

        int databaseSizeBeforeDelete = repairRequestRepository.findAll().size();

        // Delete the repairRequest
        restRepairRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, repairRequest.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RepairRequest> repairRequestList = repairRequestRepository.findAll();
        assertThat(repairRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
