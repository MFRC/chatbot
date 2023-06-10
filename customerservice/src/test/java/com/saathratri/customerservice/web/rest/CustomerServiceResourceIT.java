package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
import com.saathratri.customerservice.domain.CustomerService;
import com.saathratri.customerservice.domain.CustomerServiceEntity;
import com.saathratri.customerservice.domain.CustomerServiceUser;
import com.saathratri.customerservice.domain.FAQs;
import com.saathratri.customerservice.repository.CustomerServiceRepository;
import com.saathratri.customerservice.service.criteria.CustomerServiceCriteria;
import com.saathratri.customerservice.service.dto.CustomerServiceDTO;
import com.saathratri.customerservice.service.mapper.CustomerServiceMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link CustomerServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerServiceResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_REPORT_NUMBER = 1;
    private static final Integer UPDATED_REPORT_NUMBER = 2;
    private static final Integer SMALLER_REPORT_NUMBER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/customer-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CustomerServiceRepository customerServiceRepository;

    @Autowired
    private CustomerServiceMapper customerServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerServiceMockMvc;

    private CustomerService customerService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerService createEntity(EntityManager em) {
        CustomerService customerService = new CustomerService()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .reportNumber(DEFAULT_REPORT_NUMBER);
        return customerService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerService createUpdatedEntity(EntityManager em) {
        CustomerService customerService = new CustomerService()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reportNumber(UPDATED_REPORT_NUMBER);
        return customerService;
    }

    @BeforeEach
    public void initTest() {
        customerService = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerService() throws Exception {
        int databaseSizeBeforeCreate = customerServiceRepository.findAll().size();
        // Create the CustomerService
        CustomerServiceDTO customerServiceDTO = customerServiceMapper.toDto(customerService);
        restCustomerServiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerService in the database
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerService testCustomerService = customerServiceList.get(customerServiceList.size() - 1);
        assertThat(testCustomerService.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCustomerService.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCustomerService.getReportNumber()).isEqualTo(DEFAULT_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void createCustomerServiceWithExistingId() throws Exception {
        // Create the CustomerService with an existing ID
        customerServiceRepository.saveAndFlush(customerService);
        CustomerServiceDTO customerServiceDTO = customerServiceMapper.toDto(customerService);

        int databaseSizeBeforeCreate = customerServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerServiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerService in the database
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomerServices() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList
        restCustomerServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerService.getId().toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportNumber").value(hasItem(DEFAULT_REPORT_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerService() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get the customerService
        restCustomerServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, customerService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerService.getId().toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.reportNumber").value(DEFAULT_REPORT_NUMBER));
    }

    @Test
    @Transactional
    void getCustomerServicesByIdFiltering() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        UUID id = customerService.getId();

        defaultCustomerServiceShouldBeFound("id.equals=" + id);
        defaultCustomerServiceShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerServicesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where startDate equals to DEFAULT_START_DATE
        defaultCustomerServiceShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the customerServiceList where startDate equals to UPDATED_START_DATE
        defaultCustomerServiceShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllCustomerServicesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultCustomerServiceShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the customerServiceList where startDate equals to UPDATED_START_DATE
        defaultCustomerServiceShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllCustomerServicesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where startDate is not null
        defaultCustomerServiceShouldBeFound("startDate.specified=true");

        // Get all the customerServiceList where startDate is null
        defaultCustomerServiceShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServicesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where endDate equals to DEFAULT_END_DATE
        defaultCustomerServiceShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the customerServiceList where endDate equals to UPDATED_END_DATE
        defaultCustomerServiceShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCustomerServicesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultCustomerServiceShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the customerServiceList where endDate equals to UPDATED_END_DATE
        defaultCustomerServiceShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCustomerServicesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where endDate is not null
        defaultCustomerServiceShouldBeFound("endDate.specified=true");

        // Get all the customerServiceList where endDate is null
        defaultCustomerServiceShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServicesByReportNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where reportNumber equals to DEFAULT_REPORT_NUMBER
        defaultCustomerServiceShouldBeFound("reportNumber.equals=" + DEFAULT_REPORT_NUMBER);

        // Get all the customerServiceList where reportNumber equals to UPDATED_REPORT_NUMBER
        defaultCustomerServiceShouldNotBeFound("reportNumber.equals=" + UPDATED_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServicesByReportNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where reportNumber in DEFAULT_REPORT_NUMBER or UPDATED_REPORT_NUMBER
        defaultCustomerServiceShouldBeFound("reportNumber.in=" + DEFAULT_REPORT_NUMBER + "," + UPDATED_REPORT_NUMBER);

        // Get all the customerServiceList where reportNumber equals to UPDATED_REPORT_NUMBER
        defaultCustomerServiceShouldNotBeFound("reportNumber.in=" + UPDATED_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServicesByReportNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where reportNumber is not null
        defaultCustomerServiceShouldBeFound("reportNumber.specified=true");

        // Get all the customerServiceList where reportNumber is null
        defaultCustomerServiceShouldNotBeFound("reportNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerServicesByReportNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where reportNumber is greater than or equal to DEFAULT_REPORT_NUMBER
        defaultCustomerServiceShouldBeFound("reportNumber.greaterThanOrEqual=" + DEFAULT_REPORT_NUMBER);

        // Get all the customerServiceList where reportNumber is greater than or equal to UPDATED_REPORT_NUMBER
        defaultCustomerServiceShouldNotBeFound("reportNumber.greaterThanOrEqual=" + UPDATED_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServicesByReportNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where reportNumber is less than or equal to DEFAULT_REPORT_NUMBER
        defaultCustomerServiceShouldBeFound("reportNumber.lessThanOrEqual=" + DEFAULT_REPORT_NUMBER);

        // Get all the customerServiceList where reportNumber is less than or equal to SMALLER_REPORT_NUMBER
        defaultCustomerServiceShouldNotBeFound("reportNumber.lessThanOrEqual=" + SMALLER_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServicesByReportNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where reportNumber is less than DEFAULT_REPORT_NUMBER
        defaultCustomerServiceShouldNotBeFound("reportNumber.lessThan=" + DEFAULT_REPORT_NUMBER);

        // Get all the customerServiceList where reportNumber is less than UPDATED_REPORT_NUMBER
        defaultCustomerServiceShouldBeFound("reportNumber.lessThan=" + UPDATED_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServicesByReportNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        // Get all the customerServiceList where reportNumber is greater than DEFAULT_REPORT_NUMBER
        defaultCustomerServiceShouldNotBeFound("reportNumber.greaterThan=" + DEFAULT_REPORT_NUMBER);

        // Get all the customerServiceList where reportNumber is greater than SMALLER_REPORT_NUMBER
        defaultCustomerServiceShouldBeFound("reportNumber.greaterThan=" + SMALLER_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerServicesByFaqsIsEqualToSomething() throws Exception {
        FAQs faqs;
        if (TestUtil.findAll(em, FAQs.class).isEmpty()) {
            customerServiceRepository.saveAndFlush(customerService);
            faqs = FAQsResourceIT.createEntity(em);
        } else {
            faqs = TestUtil.findAll(em, FAQs.class).get(0);
        }
        em.persist(faqs);
        em.flush();
        customerService.setFaqs(faqs);
        customerServiceRepository.saveAndFlush(customerService);
        UUID faqsId = faqs.getId();

        // Get all the customerServiceList where faqs equals to faqsId
        defaultCustomerServiceShouldBeFound("faqsId.equals=" + faqsId);

        // Get all the customerServiceList where faqs equals to UUID.randomUUID()
        defaultCustomerServiceShouldNotBeFound("faqsId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllCustomerServicesByCustomerServiceEntityIsEqualToSomething() throws Exception {
        CustomerServiceEntity customerServiceEntity;
        if (TestUtil.findAll(em, CustomerServiceEntity.class).isEmpty()) {
            customerServiceRepository.saveAndFlush(customerService);
            customerServiceEntity = CustomerServiceEntityResourceIT.createEntity(em);
        } else {
            customerServiceEntity = TestUtil.findAll(em, CustomerServiceEntity.class).get(0);
        }
        em.persist(customerServiceEntity);
        em.flush();
        customerService.setCustomerServiceEntity(customerServiceEntity);
        customerServiceRepository.saveAndFlush(customerService);
        UUID customerServiceEntityId = customerServiceEntity.getId();

        // Get all the customerServiceList where customerServiceEntity equals to customerServiceEntityId
        defaultCustomerServiceShouldBeFound("customerServiceEntityId.equals=" + customerServiceEntityId);

        // Get all the customerServiceList where customerServiceEntity equals to UUID.randomUUID()
        defaultCustomerServiceShouldNotBeFound("customerServiceEntityId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllCustomerServicesByCustomerServiceUserIsEqualToSomething() throws Exception {
        CustomerServiceUser customerServiceUser;
        if (TestUtil.findAll(em, CustomerServiceUser.class).isEmpty()) {
            customerServiceRepository.saveAndFlush(customerService);
            customerServiceUser = CustomerServiceUserResourceIT.createEntity(em);
        } else {
            customerServiceUser = TestUtil.findAll(em, CustomerServiceUser.class).get(0);
        }
        em.persist(customerServiceUser);
        em.flush();
        customerService.setCustomerServiceUser(customerServiceUser);
        customerServiceRepository.saveAndFlush(customerService);
        UUID customerServiceUserId = customerServiceUser.getId();

        // Get all the customerServiceList where customerServiceUser equals to customerServiceUserId
        defaultCustomerServiceShouldBeFound("customerServiceUserId.equals=" + customerServiceUserId);

        // Get all the customerServiceList where customerServiceUser equals to UUID.randomUUID()
        defaultCustomerServiceShouldNotBeFound("customerServiceUserId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerServiceShouldBeFound(String filter) throws Exception {
        restCustomerServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerService.getId().toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportNumber").value(hasItem(DEFAULT_REPORT_NUMBER)));

        // Check, that the count call also returns 1
        restCustomerServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerServiceShouldNotBeFound(String filter) throws Exception {
        restCustomerServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerService() throws Exception {
        // Get the customerService
        restCustomerServiceMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerService() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        int databaseSizeBeforeUpdate = customerServiceRepository.findAll().size();

        // Update the customerService
        CustomerService updatedCustomerService = customerServiceRepository.findById(customerService.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerService are not directly saved in db
        em.detach(updatedCustomerService);
        updatedCustomerService.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).reportNumber(UPDATED_REPORT_NUMBER);
        CustomerServiceDTO customerServiceDTO = customerServiceMapper.toDto(updatedCustomerService);

        restCustomerServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerServiceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerService in the database
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeUpdate);
        CustomerService testCustomerService = customerServiceList.get(customerServiceList.size() - 1);
        assertThat(testCustomerService.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCustomerService.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCustomerService.getReportNumber()).isEqualTo(UPDATED_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingCustomerService() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceRepository.findAll().size();
        customerService.setId(UUID.randomUUID());

        // Create the CustomerService
        CustomerServiceDTO customerServiceDTO = customerServiceMapper.toDto(customerService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerServiceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerService in the database
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerService() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceRepository.findAll().size();
        customerService.setId(UUID.randomUUID());

        // Create the CustomerService
        CustomerServiceDTO customerServiceDTO = customerServiceMapper.toDto(customerService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerService in the database
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerService() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceRepository.findAll().size();
        customerService.setId(UUID.randomUUID());

        // Create the CustomerService
        CustomerServiceDTO customerServiceDTO = customerServiceMapper.toDto(customerService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerService in the database
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerServiceWithPatch() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        int databaseSizeBeforeUpdate = customerServiceRepository.findAll().size();

        // Update the customerService using partial update
        CustomerService partialUpdatedCustomerService = new CustomerService();
        partialUpdatedCustomerService.setId(customerService.getId());

        partialUpdatedCustomerService.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restCustomerServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerService.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerService))
            )
            .andExpect(status().isOk());

        // Validate the CustomerService in the database
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeUpdate);
        CustomerService testCustomerService = customerServiceList.get(customerServiceList.size() - 1);
        assertThat(testCustomerService.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCustomerService.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCustomerService.getReportNumber()).isEqualTo(DEFAULT_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateCustomerServiceWithPatch() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        int databaseSizeBeforeUpdate = customerServiceRepository.findAll().size();

        // Update the customerService using partial update
        CustomerService partialUpdatedCustomerService = new CustomerService();
        partialUpdatedCustomerService.setId(customerService.getId());

        partialUpdatedCustomerService.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).reportNumber(UPDATED_REPORT_NUMBER);

        restCustomerServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerService.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerService))
            )
            .andExpect(status().isOk());

        // Validate the CustomerService in the database
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeUpdate);
        CustomerService testCustomerService = customerServiceList.get(customerServiceList.size() - 1);
        assertThat(testCustomerService.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCustomerService.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCustomerService.getReportNumber()).isEqualTo(UPDATED_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerService() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceRepository.findAll().size();
        customerService.setId(UUID.randomUUID());

        // Create the CustomerService
        CustomerServiceDTO customerServiceDTO = customerServiceMapper.toDto(customerService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerServiceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerService in the database
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerService() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceRepository.findAll().size();
        customerService.setId(UUID.randomUUID());

        // Create the CustomerService
        CustomerServiceDTO customerServiceDTO = customerServiceMapper.toDto(customerService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerService in the database
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerService() throws Exception {
        int databaseSizeBeforeUpdate = customerServiceRepository.findAll().size();
        customerService.setId(UUID.randomUUID());

        // Create the CustomerService
        CustomerServiceDTO customerServiceDTO = customerServiceMapper.toDto(customerService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerServiceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerServiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerService in the database
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerService() throws Exception {
        // Initialize the database
        customerServiceRepository.saveAndFlush(customerService);

        int databaseSizeBeforeDelete = customerServiceRepository.findAll().size();

        // Delete the customerService
        restCustomerServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerService.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerService> customerServiceList = customerServiceRepository.findAll();
        assertThat(customerServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
