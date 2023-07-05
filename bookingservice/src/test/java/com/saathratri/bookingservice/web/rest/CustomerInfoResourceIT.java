package com.saathratri.bookingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.bookingservice.IntegrationTest;
import com.saathratri.bookingservice.domain.CustomerInfo;
import com.saathratri.bookingservice.repository.CustomerInfoRepository;
import com.saathratri.bookingservice.service.dto.CustomerInfoDTO;
import com.saathratri.bookingservice.service.mapper.CustomerInfoMapper;
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
 * Integration tests for the {@link CustomerInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerInfoResourceIT {

    private static final UUID DEFAULT_CUSTOMER_ID = UUID.randomUUID();
    private static final UUID UPDATED_CUSTOMER_ID = UUID.randomUUID();

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CustomerInfoRepository customerInfoRepository;

    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerInfoMockMvc;

    private CustomerInfo customerInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerInfo createEntity(EntityManager em) {
        CustomerInfo customerInfo = new CustomerInfo()
            .customerID(DEFAULT_CUSTOMER_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME);
        return customerInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerInfo createUpdatedEntity(EntityManager em) {
        CustomerInfo customerInfo = new CustomerInfo()
            .customerID(UPDATED_CUSTOMER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);
        return customerInfo;
    }

    @BeforeEach
    public void initTest() {
        customerInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerInfo() throws Exception {
        int databaseSizeBeforeCreate = customerInfoRepository.findAll().size();
        // Create the CustomerInfo
        CustomerInfoDTO customerInfoDTO = customerInfoMapper.toDto(customerInfo);
        restCustomerInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerInfoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerInfo in the database
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerInfo testCustomerInfo = customerInfoList.get(customerInfoList.size() - 1);
        assertThat(testCustomerInfo.getCustomerID()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testCustomerInfo.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCustomerInfo.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void createCustomerInfoWithExistingId() throws Exception {
        // Create the CustomerInfo with an existing ID
        customerInfoRepository.saveAndFlush(customerInfo);
        CustomerInfoDTO customerInfoDTO = customerInfoMapper.toDto(customerInfo);

        int databaseSizeBeforeCreate = customerInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerInfo in the database
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomerInfos() throws Exception {
        // Initialize the database
        customerInfoRepository.saveAndFlush(customerInfo);

        // Get all the customerInfoList
        restCustomerInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerInfo.getId().toString())))
            .andExpect(jsonPath("$.[*].customerID").value(hasItem(DEFAULT_CUSTOMER_ID.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));
    }

    @Test
    @Transactional
    void getCustomerInfo() throws Exception {
        // Initialize the database
        customerInfoRepository.saveAndFlush(customerInfo);

        // Get the customerInfo
        restCustomerInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, customerInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerInfo.getId().toString()))
            .andExpect(jsonPath("$.customerID").value(DEFAULT_CUSTOMER_ID.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCustomerInfo() throws Exception {
        // Get the customerInfo
        restCustomerInfoMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerInfo() throws Exception {
        // Initialize the database
        customerInfoRepository.saveAndFlush(customerInfo);

        int databaseSizeBeforeUpdate = customerInfoRepository.findAll().size();

        // Update the customerInfo
        CustomerInfo updatedCustomerInfo = customerInfoRepository.findById(customerInfo.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerInfo are not directly saved in db
        em.detach(updatedCustomerInfo);
        updatedCustomerInfo.customerID(UPDATED_CUSTOMER_ID).firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);
        CustomerInfoDTO customerInfoDTO = customerInfoMapper.toDto(updatedCustomerInfo);

        restCustomerInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerInfoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerInfo in the database
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeUpdate);
        CustomerInfo testCustomerInfo = customerInfoList.get(customerInfoList.size() - 1);
        assertThat(testCustomerInfo.getCustomerID()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCustomerInfo.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomerInfo.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCustomerInfo() throws Exception {
        int databaseSizeBeforeUpdate = customerInfoRepository.findAll().size();
        customerInfo.setId(UUID.randomUUID());

        // Create the CustomerInfo
        CustomerInfoDTO customerInfoDTO = customerInfoMapper.toDto(customerInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerInfoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerInfo in the database
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerInfo() throws Exception {
        int databaseSizeBeforeUpdate = customerInfoRepository.findAll().size();
        customerInfo.setId(UUID.randomUUID());

        // Create the CustomerInfo
        CustomerInfoDTO customerInfoDTO = customerInfoMapper.toDto(customerInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerInfo in the database
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerInfo() throws Exception {
        int databaseSizeBeforeUpdate = customerInfoRepository.findAll().size();
        customerInfo.setId(UUID.randomUUID());

        // Create the CustomerInfo
        CustomerInfoDTO customerInfoDTO = customerInfoMapper.toDto(customerInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerInfoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerInfo in the database
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerInfoWithPatch() throws Exception {
        // Initialize the database
        customerInfoRepository.saveAndFlush(customerInfo);

        int databaseSizeBeforeUpdate = customerInfoRepository.findAll().size();

        // Update the customerInfo using partial update
        CustomerInfo partialUpdatedCustomerInfo = new CustomerInfo();
        partialUpdatedCustomerInfo.setId(customerInfo.getId());

        partialUpdatedCustomerInfo.firstName(UPDATED_FIRST_NAME);

        restCustomerInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerInfo))
            )
            .andExpect(status().isOk());

        // Validate the CustomerInfo in the database
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeUpdate);
        CustomerInfo testCustomerInfo = customerInfoList.get(customerInfoList.size() - 1);
        assertThat(testCustomerInfo.getCustomerID()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testCustomerInfo.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomerInfo.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCustomerInfoWithPatch() throws Exception {
        // Initialize the database
        customerInfoRepository.saveAndFlush(customerInfo);

        int databaseSizeBeforeUpdate = customerInfoRepository.findAll().size();

        // Update the customerInfo using partial update
        CustomerInfo partialUpdatedCustomerInfo = new CustomerInfo();
        partialUpdatedCustomerInfo.setId(customerInfo.getId());

        partialUpdatedCustomerInfo.customerID(UPDATED_CUSTOMER_ID).firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restCustomerInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerInfo))
            )
            .andExpect(status().isOk());

        // Validate the CustomerInfo in the database
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeUpdate);
        CustomerInfo testCustomerInfo = customerInfoList.get(customerInfoList.size() - 1);
        assertThat(testCustomerInfo.getCustomerID()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCustomerInfo.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomerInfo.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerInfo() throws Exception {
        int databaseSizeBeforeUpdate = customerInfoRepository.findAll().size();
        customerInfo.setId(UUID.randomUUID());

        // Create the CustomerInfo
        CustomerInfoDTO customerInfoDTO = customerInfoMapper.toDto(customerInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerInfoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerInfo in the database
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerInfo() throws Exception {
        int databaseSizeBeforeUpdate = customerInfoRepository.findAll().size();
        customerInfo.setId(UUID.randomUUID());

        // Create the CustomerInfo
        CustomerInfoDTO customerInfoDTO = customerInfoMapper.toDto(customerInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerInfo in the database
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerInfo() throws Exception {
        int databaseSizeBeforeUpdate = customerInfoRepository.findAll().size();
        customerInfo.setId(UUID.randomUUID());

        // Create the CustomerInfo
        CustomerInfoDTO customerInfoDTO = customerInfoMapper.toDto(customerInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerInfoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerInfo in the database
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerInfo() throws Exception {
        // Initialize the database
        customerInfoRepository.saveAndFlush(customerInfo);

        int databaseSizeBeforeDelete = customerInfoRepository.findAll().size();

        // Delete the customerInfo
        restCustomerInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerInfo.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerInfo> customerInfoList = customerInfoRepository.findAll();
        assertThat(customerInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
