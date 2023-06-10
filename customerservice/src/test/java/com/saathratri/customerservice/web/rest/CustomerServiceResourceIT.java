package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
import com.saathratri.customerservice.domain.CustomerService;
import com.saathratri.customerservice.repository.CustomerServiceRepository;
import com.saathratri.customerservice.service.criteria.CustomerServiceCriteria;
import com.saathratri.customerservice.service.dto.CustomerServiceDTO;
import com.saathratri.customerservice.service.mapper.CustomerServiceMapper;
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
        CustomerService customerService = new CustomerService();
        return customerService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerService createUpdatedEntity(EntityManager em) {
        CustomerService customerService = new CustomerService();
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerService.getId().toString())));
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
            .andExpect(jsonPath("$.id").value(customerService.getId().toString()));
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

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerServiceShouldBeFound(String filter) throws Exception {
        restCustomerServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerService.getId().toString())));

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
