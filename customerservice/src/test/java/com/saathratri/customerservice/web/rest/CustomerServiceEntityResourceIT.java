package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
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
        CustomerServiceEntity customerServiceEntity = new CustomerServiceEntity();
        return customerServiceEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerServiceEntity createUpdatedEntity(EntityManager em) {
        CustomerServiceEntity customerServiceEntity = new CustomerServiceEntity();
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerServiceEntity.getId().toString())));
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
            .andExpect(jsonPath("$.id").value(customerServiceEntity.getId().toString()));
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

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerServiceEntityShouldBeFound(String filter) throws Exception {
        restCustomerServiceEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerServiceEntity.getId().toString())));

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
