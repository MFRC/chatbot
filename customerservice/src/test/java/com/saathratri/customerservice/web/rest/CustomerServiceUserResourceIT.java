package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
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
        CustomerServiceUser customerServiceUser = new CustomerServiceUser();
        return customerServiceUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerServiceUser createUpdatedEntity(EntityManager em) {
        CustomerServiceUser customerServiceUser = new CustomerServiceUser();
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerServiceUser.getId().toString())));
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
            .andExpect(jsonPath("$.id").value(customerServiceUser.getId().toString()));
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

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerServiceUserShouldBeFound(String filter) throws Exception {
        restCustomerServiceUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerServiceUser.getId().toString())));

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
